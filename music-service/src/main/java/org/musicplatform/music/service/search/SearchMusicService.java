package org.musicplatform.music.service.search;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SearchMusicService {

    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;
    private final SoundRepository soundRepository;
    private final SoundMapper soundMapper;

    @Autowired
    public SearchMusicService(
            ArtistRepository artistRepository, AlbumRepository albumRepository,
            AlbumMapper albumMapper, SoundRepository soundRepository,
            SoundMapper soundMapper) {
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
        this.albumMapper = albumMapper;
        this.soundRepository = soundRepository;
        this.soundMapper = soundMapper;
    }

    public PageResponse<SoundResponse> getTracksByTitleStartingWith(String fragment, int page, int size){
        Page<Sound> soundsPage =  soundRepository.findByTitleStartingWithIgnoreCase
                (fragment, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
        List<Sound> sounds = checkForEmptyContent(soundsPage,
                "Треки по запросу " + "\"" + fragment + "\"" + " не найдены");
        List<SoundResponse> soundResponseList = sounds.stream().map(soundMapper::toResponse).toList();
        return new PageResponse<>(soundResponseList, soundsPage.hasNext());
    }

    public PageResponse<AlbumResponse> getAlbumsByTitleStartingWith(String fragment, int page, int size){
        Page<Album> albumsPage = albumRepository.findByTitleStartingWithIgnoreCase
                (fragment, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
        List<Album> albums = checkForEmptyContent(albumsPage,
                "Альбомы по запросу " + "\"" + fragment + "\"" + " не найдены");
        List<AlbumResponse> albumResponseList = albums.stream().map(albumMapper::toAlbumResponse).toList();
        return new PageResponse<>(albumResponseList, albumsPage.hasNext());
    }

    public PageResponse<ArtistResponse> getArtistsByNameStartingWith(String fragment, int page, int size){
        Page<Artist> artistsPage = artistRepository.findByNameStartingWithIgnoreCase
                (fragment, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
        List<Artist> artists = checkForEmptyContent(artistsPage,
                "Испольнители по запросу " + "\"" + fragment + "\"" + " не найдены");
        List<ArtistResponse> artistResponseList = artists.stream().map
                (artist -> new ArtistResponse(artist.getId(), artist.getName())).toList();
        return new PageResponse<>(artistResponseList, artistsPage.hasNext());
    }

    private <T> List<T> checkForEmptyContent(Page<T> paging, String exceptionMessage){
        List<T> content = paging.getContent();
        if(content.isEmpty()) throw new NoSuchMusicException(exceptionMessage);
        return content;
    }
}
