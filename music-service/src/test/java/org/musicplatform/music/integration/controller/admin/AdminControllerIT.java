package org.musicplatform.music.integration.controller.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.musicplatform.music.dto.metadata.TrackMetadata;
import org.musicplatform.music.entity.genre.GenreName;
import org.musicplatform.music.entity.music.Sound;
import org.musicplatform.music.entity.user.Authority;
import org.musicplatform.music.integration.jamendo.JamendoClient;
import org.musicplatform.music.integration.jamendo.response.MusicResponse;
import org.musicplatform.music.repository.music.SoundRepository;
import org.musicplatform.music.service.uploadData.TrackMetadataWriter;
import org.musicplatform.music.support.config.AbstractSpringBootIT;
import org.musicplatform.music.support.factory.it.music.MusicFactoryIT;
import org.musicplatform.music.support.factory.it.security.WithMockUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminControllerIT extends AbstractSpringBootIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SoundRepository soundRepository;

    @MockitoBean
    private JamendoClient jamendoClient;
    @MockitoBean
    private TrackMetadataWriter trackMetadataWriter;

    @BeforeEach
    void cleanData(){
        jdbcTemplate.execute("TRUNCATE TABLE artist," +
                " album, album_image, sound RESTART IDENTITY CASCADE");
    }

    private final String genreName = GenreName.BLUES.name();
    private final int sizeApiContent = 100;


    @Test
    @WithMockUserPrincipal(authority = Authority.ADMIN)
    void shouldSuccessfulSaveAndWriteRecords() throws Exception {
        List<MusicResponse> apiResponseList = MusicFactoryIT.musicResponseList(sizeApiContent);
        List<String> apiResponseTrackNames = apiResponseList.stream().map(MusicResponse::name).toList();
        when(jamendoClient.tracksPack(genreName)).thenReturn(apiResponseList);

        RequestBuilder requestBuilder = post("/api/admin/import").param("genreName", genreName);
        mockMvc.perform(requestBuilder).andExpect(status().isAccepted());

        List<Sound> sounds = soundRepository.findAll();

        assertThat(sounds.size()).isEqualTo(sizeApiContent);
        assertThat(sounds).extracting(Sound::getTitle).containsExactlyInAnyOrderElementsOf(apiResponseTrackNames);

        verify(trackMetadataWriter, times(sizeApiContent)).write(any(TrackMetadata.class));
    }
    @Test
    @WithMockUserPrincipal(authority = Authority.ADMIN)
    void shouldSuccessfulSaveAndWriteWithoutDuplicates() throws Exception {
        List<MusicResponse> apiResponseList = MusicFactoryIT.musicResponseList(sizeApiContent);
        when(jamendoClient.tracksPack(genreName)).thenReturn(apiResponseList);

        RequestBuilder firstRequestBuilder = post("/api/admin/import").param("genreName", genreName);
        mockMvc.perform(firstRequestBuilder).andExpect(status().isAccepted());

        RequestBuilder secondRequestBuilder = post("/api/admin/import").param("genreName", genreName);
        mockMvc.perform(secondRequestBuilder).andExpect(status().isAccepted());

        List<Sound> sounds = soundRepository.findAll();

        assertThat(sounds).extracting(Sound::getTitle).doesNotHaveDuplicates();

        verify(trackMetadataWriter, times(sizeApiContent)).write(any(TrackMetadata.class));
    }

    @Test
    @WithMockUserPrincipal(authority = Authority.ADMIN)
    void shouldReturnNotFoundStatus_WhenGenreNameIsInvalid() throws Exception {
        String genreName = "incorrect genre name";

        mockMvc.perform(post("/api/admin/import").param("genreName", genreName))
                .andExpect(status().isNotFound());

        assertThat(soundRepository.findAll()).isEmpty();
        verifyNoInteractions(trackMetadataWriter);
    }
}
