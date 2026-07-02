package org.musicplatform.music.unit.service.search;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.musicplatform.music.dto.music.album.AlbumResponse;
import org.musicplatform.music.dto.music.artist.ArtistResponse;
import org.musicplatform.music.dto.music.common.PageResponse;
import org.musicplatform.music.dto.music.sound.SoundResponse;
import org.musicplatform.music.entity.music.Album;
import org.musicplatform.music.entity.music.Artist;
import org.musicplatform.music.entity.music.Sound;
import org.musicplatform.music.exception.music.NoSuchMusicException;
import org.musicplatform.music.mapper.album.AlbumMapper;
import org.musicplatform.music.mapper.sound.SoundMapper;
import org.musicplatform.music.repository.music.AlbumRepository;
import org.musicplatform.music.repository.music.ArtistRepository;
import org.musicplatform.music.repository.music.SoundRepository;
import org.musicplatform.music.service.search.SearchMusicService;
import org.musicplatform.music.support.factory.unit.MusicDataFactory;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchMusicServiceTest {

    @Mock
    private ArtistRepository artistRepository;
    @Mock
    private AlbumRepository albumRepository;
    @Mock
    private AlbumMapper albumMapper;
    @Mock
    private SoundRepository soundRepository;
    @Mock
    private SoundMapper soundMapper;

    @InjectMocks
    private SearchMusicService searchMusicService;

    private final String fragment = "just dance";
    private final int page = 0;
    private final int size = 2;
    private final Pageable pageableWithSortByIdAsc = PageRequest.of
            (page, size, Sort.by(Sort.Direction.ASC, "id"));

    @Test
    void getTracksByTitleStartingWith_ShouldReturnPageResponseOfSounds(){
        Sound sound1 = new Sound();
        Sound sound2 = new Sound();
        Page<Sound> soundsPage = new PageImpl<>(List.of(sound1, sound2), pageableWithSortByIdAsc, 3);

        SoundResponse soundResponse = MusicDataFactory.soundResponse();

        when(soundRepository.findByTitleStartingWithIgnoreCase
                (fragment, pageableWithSortByIdAsc)).thenReturn(soundsPage);
        when(soundMapper.toResponse(any(Sound.class))).thenReturn(soundResponse);

        PageResponse<SoundResponse> pageResponse = searchMusicService.getTracksByTitleStartingWith(fragment, page, size);
        assertPageResponse(pageResponse);
        assertEquals(soundResponse, pageResponse.content().get(0));
        assertEquals(soundResponse, pageResponse.content().get(1));
    }

    @Test
    void getTracksByTitleStartingWith_ShouldThrowNoSuchMusicException_WhenPageContentIsEmpty(){
        Page<Sound> soundsPage = new PageImpl<>(List.of(), pageableWithSortByIdAsc, 0);

        when(soundRepository.findByTitleStartingWithIgnoreCase
                (fragment, pageableWithSortByIdAsc)).thenReturn(soundsPage);

        assertThrows(NoSuchMusicException.class, () -> searchMusicService
                .getTracksByTitleStartingWith(fragment, page, size));

        verify(soundRepository).findByTitleStartingWithIgnoreCase(fragment, pageableWithSortByIdAsc);
        verifyNoInteractions(soundMapper);
    }

    @Test
    void getAlbumsByTitleStartingWith_ShouldReturnPageResponseOfAlbums(){
        Album album1 = new Album();
        Album album2 = new Album();
        Page<Album> albumsPage = new PageImpl<>(List.of(album1, album2), pageableWithSortByIdAsc, 3);

        AlbumResponse albumResponse = MusicDataFactory.albumResponse();

        when(albumRepository.findByTitleStartingWithIgnoreCase
                (fragment, pageableWithSortByIdAsc)).thenReturn(albumsPage);
        when(albumMapper.toAlbumResponse(any(Album.class))).thenReturn(albumResponse);

        PageResponse<AlbumResponse> pageResponse = searchMusicService.getAlbumsByTitleStartingWith(fragment, page, size);
        assertPageResponse(pageResponse);
        assertEquals(albumResponse, pageResponse.content().get(0));
        assertEquals(albumResponse, pageResponse.content().get(1));
    }

    @Test
    void getAlbumsByTitleStartingWith_ShouldThrowNoSuchMusicException_WhenPageContentIsEmpty(){
        Page<Album> albumsPage = new PageImpl<>(List.of(), pageableWithSortByIdAsc, 0);

        when(albumRepository.findByTitleStartingWithIgnoreCase
                (fragment, pageableWithSortByIdAsc)).thenReturn(albumsPage);

        assertThrows(NoSuchMusicException.class, () -> searchMusicService
                .getAlbumsByTitleStartingWith(fragment, page, size));

        verify(albumRepository).findByTitleStartingWithIgnoreCase(fragment, pageableWithSortByIdAsc);
        verifyNoInteractions(albumMapper);
    }

    @Test
    void getArtistsByNameStartingWith_ShouldReturnPageResponseOfArtists(){
        Artist artist1 = new Artist();
        Artist artist2 = new Artist();
        Page<Artist> artistsPage = new PageImpl<>(List.of(artist1, artist2), pageableWithSortByIdAsc, 3);

        when(artistRepository.findByNameStartingWithIgnoreCase
                (fragment, pageableWithSortByIdAsc)).thenReturn(artistsPage);

        PageResponse<ArtistResponse> pageResponse = searchMusicService.getArtistsByNameStartingWith(fragment, page, size);
        assertPageResponse(pageResponse);
    }

    @Test
    void getArtistsByNameStartingWith_ShouldThrowNoSuchMusicException_WhenPageContentIsEmpty(){
        Page<Artist> artistsPage = new PageImpl<>(List.of(), pageableWithSortByIdAsc, 3);

        when(artistRepository.findByNameStartingWithIgnoreCase
                (fragment, pageableWithSortByIdAsc)).thenReturn(artistsPage);

        assertThrows(NoSuchMusicException.class, () -> searchMusicService.getArtistsByNameStartingWith(fragment, page, size));
        verify(artistRepository).findByNameStartingWithIgnoreCase(fragment, pageableWithSortByIdAsc);
    }

    private <T> void assertPageResponse(PageResponse<T> pageResponse){
        assertEquals(size, pageResponse.content().size());
        assertTrue(pageResponse.hasNextPage());
    }
}
