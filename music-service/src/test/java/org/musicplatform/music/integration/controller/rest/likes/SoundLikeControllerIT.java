package org.musicplatform.music.integration.controller.rest.likes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.musicplatform.music.dto.likes.LikeStatusResponse;
import org.musicplatform.music.entity.genre.Genre;
import org.musicplatform.music.entity.genre.GenreName;
import org.musicplatform.music.entity.likes.SoundLike;
import org.musicplatform.music.entity.music.Sound;
import org.musicplatform.music.repository.likes.SoundLikeRepository;
import org.musicplatform.music.repository.music.GenreRepository;
import org.musicplatform.music.support.config.AbstractSpringBootIT;
import org.musicplatform.music.support.factory.it.MusicFactoryIT;
import org.musicplatform.music.support.factory.it.security.WithMockUserPrincipal;
import org.musicplatform.music.support.fixture.integration.SoundTestFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SoundTestFixture.class)
public class SoundLikeControllerIT extends AbstractSpringBootIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SoundTestFixture soundFixture;
    @Autowired
    private SoundLikeRepository soundLikeRepository;
    @Autowired
    private GenreRepository genreRepository;

    private Genre genre;

    private static final long userId = 1L;

    @BeforeEach
    void setup(){
        truncateTables();
    }

    @BeforeAll
    void getGenre(){
        genre = genreRepository.findByName(GenreName.ROCK).orElseThrow();
    }

    private void truncateTables(){
        jdbcTemplate.execute("TRUNCATE TABLE artist, album, album_image," +
                " sound, sound_like, album_like RESTART IDENTITY CASCADE");
    }

    @Test
    @WithMockUserPrincipal(userId = userId)
    void shouldReturnsLikeStatusIsTrueAndHttpStatusIsOk_WhenSoundLikeExists() throws Exception{
        Sound sound = soundFixture.soundAggregateWithOneSound(genre).sounds().getFirst();
        soundLikeRepository.save(MusicFactoryIT.soundLike(userId, sound));

        MvcResult result = mockMvc.perform(get("/api/private/sound-like/is-liked/{id}", sound.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likeStatus").exists())
                .andReturn();

        String resultJson = result.getResponse().getContentAsString();
        LikeStatusResponse likeStatusResponse = objectMapper.readValue(resultJson, LikeStatusResponse.class);
        assertThat(likeStatusResponse.likeStatus()).isTrue();
    }

    @Test
    @WithMockUserPrincipal(userId = userId)
    void shouldReturnsLikeStatusIsFalseAndHttpStatusIsOk_WhenSoundLikeIsNotExists() throws Exception{
        MvcResult result = mockMvc.perform(get("/api/private/sound-like/is-liked/{id}", 256L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likeStatus").exists())
                .andReturn();

        String resultJson = result.getResponse().getContentAsString();
        LikeStatusResponse likeStatusResponse = objectMapper.readValue(resultJson, LikeStatusResponse.class);
        assertThat(likeStatusResponse.likeStatus()).isFalse();
    }

    @Test
    @WithMockUserPrincipal(userId = userId)
    void shouldCreateSoundLikeAndReturnStatusIsCreated() throws Exception{
        Sound sound = soundFixture.soundAggregateWithOneSound(genre).sounds().getFirst();

        mockMvc.perform(post("/api/private/sound-like/{id}", sound.getId()))
                .andExpect(status().isCreated());

        assertThat(soundLikeRepository.count()).isEqualTo(1);

        SoundLike soundLike = soundLikeRepository.findAll().getFirst();
        assertThat(soundLike.getSound().getId()).isEqualTo(sound.getId());
        assertThat(soundLike.getUserId()).isEqualTo(userId);
    }

    @Test
    @WithMockUserPrincipal(userId = userId)
    void shouldDeleteSoundLikeAndReturnStatusIsNoContent() throws Exception{
        Sound sound = soundFixture.soundAggregateWithOneSound(genre).sounds().getFirst();
        SoundLike soundLike = soundLikeRepository.save(MusicFactoryIT.soundLike(userId, sound));

        mockMvc.perform(delete("/api/private/sound-like/{id}", sound.getId()))
                .andExpect(status().isNoContent());

        assertThat(soundLikeRepository.findById(soundLike.getId())).isEmpty();
    }
}
