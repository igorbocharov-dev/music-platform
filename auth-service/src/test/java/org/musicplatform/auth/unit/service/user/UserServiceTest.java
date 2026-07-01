package org.musicplatform.auth.unit.service.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.musicplatform.auth.dto.ImageResponse;
import org.musicplatform.auth.dto.user.RegistrationRequest;
import org.musicplatform.auth.dto.user.UserMainResponse;
import org.musicplatform.auth.entity.Authority;
import org.musicplatform.auth.entity.User;
import org.musicplatform.auth.entity.UserAvatar;
import org.musicplatform.auth.exception.UserNotFoundException;
import org.musicplatform.auth.mapper.ImageMapper;
import org.musicplatform.auth.repository.UserAvatarRepository;
import org.musicplatform.auth.repository.UserRepository;
import org.musicplatform.auth.service.user.UserService;
import org.musicplatform.auth.support.factory.unit.user.UserDataFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserAvatarRepository userAvatarRepository;
    @Mock
    private ImageMapper imageMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_ReturnValidUser(){
        RegistrationRequest registrationRequest = UserDataFactory.registrationRequest();
        String encodingPassword = "encoded";

        when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn(encodingPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // вернули тот же объект, что был передан в save()

        User result = userService.create(registrationRequest);

        assertEquals(registrationRequest.getUsername(), result.getUsername());
        assertEquals(encodingPassword, result.getPassword());
        assertEquals(registrationRequest.getEmail(), result.getEmail());
        assertEquals(registrationRequest.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(Authority.USER, result.getRole());

        verify(passwordEncoder).encode(registrationRequest.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void mainResponse_ReturnValidResponse(){
        User user = UserDataFactory.user();
        Long userId = user.getId();
        String username = user.getUsername();
        UserAvatar userAvatar = UserDataFactory.userAvatar(user);
        ImageResponse expectedAvatarResponse = UserDataFactory.avatarResponse();
        when(userRepository.getUsernameById(userId)).thenReturn(Optional.of(username));
        when(userAvatarRepository.findByUserId(userId)).thenReturn(userAvatar);
        when(imageMapper.toImageResponse(userAvatar)).thenReturn(expectedAvatarResponse);

        UserMainResponse result = userService.mainResponse(userId);
        assertEquals(username, result.username());
        assertEquals(result.avatar().key(), expectedAvatarResponse.key());
        assertEquals(result.avatar().url(), expectedAvatarResponse.url());
    }

    @Test
    void mainResponse_ThrowUserNotFoundException_WhenUsernameIsEmpty(){
        when(userRepository.getUsernameById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.mainResponse(any(Long.class)));
    }

}
