package org.musicplatform.music.support.fixture.integration;

import org.musicplatform.music.entity.genre.Genre;
import org.musicplatform.music.entity.image.AlbumImage;
import org.musicplatform.music.entity.music.Album;
import org.musicplatform.music.entity.music.Artist;
import org.musicplatform.music.repository.image.AlbumImageRepository;
import org.musicplatform.music.repository.music.AlbumRepository;
import org.musicplatform.music.repository.music.ArtistRepository;
import org.musicplatform.music.support.factory.it.MusicFactoryIT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.musicplatform.music.support.assertions.PageAssertions.totalElements;

@TestComponent
public class AlbumTestFixture {

    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private AlbumImageRepository albumImageRepository;

    public AlbumAggregate albumAggregateWithOneAlbum(Genre genre){
        Artist artist = artistRepository.save(MusicFactoryIT.artist(genre));
        Album album = albumRepository.save(MusicFactoryIT.album(artist, genre));
        AlbumImage albumImage = albumImageRepository.save(MusicFactoryIT.albumImage(album));
        album.setImage(albumImage);
        return new AlbumAggregate(artist, List.of(album));
    }

    public AlbumAggregate albumAggregateWithAlbums(Genre genre, String titlePrefix, String imgKeyNameEndsWith){
        List<Album> albums = new ArrayList<>();
        Artist artist = artistRepository.save(MusicFactoryIT.artist(genre));
        for (int i = 0; i < totalElements; i++) {
            Album album = albumRepository.save
                    (new Album(titlePrefix + "_" + i,
                            LocalDate.of(2004, 10, 15),
                            artist, genre));
            album.setImage(albumImageRepository.save
                    (new AlbumImage(i + "_" + imgKeyNameEndsWith, album)));
            albums.add(album);
        }
        return new AlbumAggregate(artist, albums);
    }
}
