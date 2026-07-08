package org.musicplatform.music.integration.controller.rest.likes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.musicplatform.music.dto.likes.LikeStatusResponse;
import org.musicplatform.music.entity.genre.Genre;
import org.musicplatform.music.entity.genre.GenreName;
import org.musicplatform.music.entity.likes.AlbumLike;
import org.musicplatform.music.entity.music.Album;
import org.musicplatform.music.repository.likes.AlbumLikeRepository;
import org.musicplatform.music.repository.music.GenreRepository;
import org.musicplatform.music.support.config.AbstractSpringBootIT;
import org.musicplatform.music.support.factory.it.MusicFactoryIT;
import org.musicplatform.music.support.factory.it.security.WithMockUserPrincipal;
import org.musicplatform.music.support.fixture.integration.AlbumTestFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(AlbumTestFixture.class)
public class AlbumLikeControllerIT extends AbstractSpringBootIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AlbumTestFixture albumFixture;
    @Autowired
    private AlbumLikeRepository albumLikeRepository;
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
    void shouldReturnsLikeStatusIsTrue_WhenAlbumLikeExists() throws Exception {
        Album album = albumFixture.albumAggregateWithOneAlbum(genre).albums().getFirst();
        albumLikeRepository.save(MusicFactoryIT.albumLike(userId, album));

        MvcResult result = mockMvc.perform(get("/api/private/album-like/is-liked/{id}", album.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likeStatus").exists())
                .andReturn();

        String resultJson = result.getResponse().getContentAsString();
        LikeStatusResponse likeStatusResponse = objectMapper.readValue(resultJson, LikeStatusResponse.class);
        assertThat(likeStatusResponse.likeStatus()).isTrue();
    }

    @Test
    @WithMockUserPrincipal(userId = userId)
    void shouldReturnsLikeStatusIsFalse_WhenAlbumLikeIsNotExists() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/private/album-like/is-liked/{id}", 256L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likeStatus").exists())
                .andReturn();

        String resultJson = result.getResponse().getContentAsString();
        LikeStatusResponse likeStatusResponse = objectMapper.readValue(resultJson, LikeStatusResponse.class);
        assertThat(likeStatusResponse.likeStatus()).isFalse();
    }

    @Test
    @WithMockUserPrincipal(userId = userId)
    void shouldCreateAlbumLike() throws Exception{
        Album album = albumFixture.albumAggregateWithOneAlbum(genre).albums().getFirst();

        mockMvc.perform(post("/api/private/album-like/{id}", album.getId()))
                .andExpect(status().isCreated());

        assertThat(albumLikeRepository.count()).isEqualTo(1);

        AlbumLike albumLike = albumLikeRepository.findAll().getFirst();
        assertThat(albumLike.getAlbum().getId()).isEqualTo(album.getId());
        assertThat(albumLike.getUserId()).isEqualTo(userId);
    }

    @Test
    @WithMockUserPrincipal(userId = userId)
    void shouldSuccessDeleteAlbumLike() throws Exception{
        Album album = albumFixture.albumAggregateWithOneAlbum(genre).albums().getFirst();
        AlbumLike albumLike = albumLikeRepository.save(MusicFactoryIT.albumLike(userId, album));

        mockMvc.perform(delete("/api/private/album-like/{id}", album.getId()))
                .andExpect(status().isNoContent());

        assertThat(albumLikeRepository.findById(albumLike.getId())).isEmpty();
    }

}
