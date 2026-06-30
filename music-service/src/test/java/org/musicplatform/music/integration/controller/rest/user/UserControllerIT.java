package org.musicplatform.music.integration.controller.rest.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.musicplatform.music.dto.user.UserMainResponse;
import org.musicplatform.music.entity.image.UserAvatar;
import org.musicplatform.music.entity.user.User;
import org.musicplatform.music.error.ApiErrorResponse;
import org.musicplatform.music.error.auth.AuthErrorCode;
import org.musicplatform.music.repository.image.UserAvatarRepository;
import org.musicplatform.music.repository.user.UserRepository;
import org.musicplatform.music.security.dto.TokenSubject;
import org.musicplatform.music.security.jwt.JwtTokenService;
import org.musicplatform.music.support.config.AbstractSpringBootIT;
import org.musicplatform.music.support.factory.it.user.UserAvatarFactoryIT;
import org.musicplatform.music.support.factory.it.user.UserDataFactoryIT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerIT extends AbstractSpringBootIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAvatarRepository userAvatarRepository;
    @Autowired
    private JwtTokenService jwtTokenService;

    private User user;

    @BeforeEach
    void setup(){
        truncateTables();
        this.user = userRepository.save(UserDataFactoryIT.userWithEnabledAccount());
    }

    private void truncateTables(){
        jdbcTemplate.execute("TRUNCATE TABLE users, user_avatar RESTART IDENTITY CASCADE");
    }

    @Test
    void shouldReturnValidUserMainResponseAndStatusIsOk() throws Exception {
        UserAvatar userAvatar = userAvatarRepository.save(UserAvatarFactoryIT.userAvatar(user));
        String jwtToken = jwtTokenService.generateToken(new TokenSubject(user.getId(), List.of(user.getRole().getAuthority())));

        MvcResult result = mockMvc.perform(get("/api/user/profile")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andReturn();

        String resultJson = result.getResponse().getContentAsString();
        UserMainResponse response = objectMapper.readValue(resultJson, UserMainResponse.class);
        assertThat(response.username()).isEqualTo(user.getUsername());
        assertThat(response.avatar().key()).isEqualTo(userAvatar.getKey());
    }

    @Test
    void shouldReturnStatusIsUnauthorized_WhenJwtTokenIsMissing() throws Exception {
        userAvatarRepository.save(UserAvatarFactoryIT.userAvatar(user));

        MvcResult result = mockMvc.perform(get("/api/user/profile"))
                .andExpect(status().isUnauthorized())
                .andReturn();

        assertAuthenticationError(result);
    }

    @Test
    void shouldReturnStatusIsUnauthorized_WhenJwtTokenInvalid() throws Exception {
        userAvatarRepository.save(UserAvatarFactoryIT.userAvatar(user));
        String jwtToken = jwtTokenService.generateToken(new TokenSubject(user.getId(), List.of(user.getRole().getAuthority())));

        MvcResult result = mockMvc.perform(get("/api/user/profile")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken + "invalid.token"))
                .andExpect(status().isUnauthorized())
                .andReturn();

        assertAuthenticationError(result);
    }

    private void assertAuthenticationError(MvcResult result) throws Exception{
        String resultJson = result.getResponse().getContentAsString();
        ApiErrorResponse errorResponse = objectMapper.readValue(resultJson, ApiErrorResponse.class);
        assertThat(errorResponse.code()).isEqualTo(AuthErrorCode.BAD_AUTHENTICATION_REQUEST.name());
        assertThat(errorResponse.status()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(errorResponse.message()).isNotEmpty();
        assertThat(errorResponse.timestamp()).isPositive();
    }
}
