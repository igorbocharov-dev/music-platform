package org.musicplatform.music.service.uploadData;

import org.musicplatform.music.entity.genre.Genre;
import org.musicplatform.music.entity.image.AlbumImage;
import org.musicplatform.music.entity.music.Album;
import org.musicplatform.music.entity.music.Artist;
import org.musicplatform.music.entity.music.Sound;
import org.musicplatform.music.integration.jamendo.response.MusicResponse;
import org.musicplatform.music.repository.image.AlbumImageRepository;
import org.musicplatform.music.repository.music.AlbumRepository;
import org.musicplatform.music.repository.music.ArtistRepository;
import org.musicplatform.music.repository.music.SoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MusicCatalogService {

    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final SoundRepository soundRepository;
    private final AlbumImageRepository albumImageRepository;

    @Autowired
    public MusicCatalogService(ArtistRepository artistRepository, AlbumRepository albumRepository,
                               SoundRepository soundRepository, AlbumImageRepository albumImageRepository) {
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
        this.soundRepository = soundRepository;
        this.albumImageRepository = albumImageRepository;
    }

    private Artist createOrGetArtist(MusicResponse response, Genre genre) {
        return artistRepository.findByName(response.artist_name())
                .orElseGet(() -> artistRepository.save(new Artist(response.artist_name(), genre)));
    }

    private Album createAlbumWithImageOrGet(MusicResponse response, Artist artist, Genre genre) {
        return albumRepository.findByTitle(response.album_name())
                .orElseGet(() -> {
                    Album newAlbum = albumRepository.save
                            (new Album(response.album_name(), response.releasedate(), artist, genre));
                    AlbumImage albumImage = albumImageRepository.save
                            (new AlbumImage(response.albumImgKey(), newAlbum));
                    newAlbum.setImage(albumImage);
                    return newAlbum;
                });
    }

    private void createSound(MusicResponse response, Artist artist, Album album, Genre genre) {
        soundRepository.save(new Sound(response.name(), response.duration(), artist, album,
                response.mp3Key(), response.releasedate(), genre));
    }

    @Transactional
    public boolean saveMusicData(MusicResponse response, Genre genre) {
        if(soundRepository.existsByTitle(response.name())) return false;
        Artist artist = createOrGetArtist(response, genre);
        Album album = createAlbumWithImageOrGet(response, artist, genre);
        createSound(response, artist, album, genre);
        return true;
    }

}
