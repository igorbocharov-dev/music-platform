package org.musicplatform.music.support.fixture.integration;

import org.musicplatform.music.entity.genre.Genre;
import org.musicplatform.music.entity.music.Artist;
import org.musicplatform.music.repository.music.ArtistRepository;
import org.musicplatform.music.support.factory.it.music.MusicFactoryIT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import static org.musicplatform.music.support.assertions.PageAssertions.totalElements;

@TestComponent
public class ArtistTestFixture {

    @Autowired
    private ArtistRepository artistRepository;

    public Artist createArtist(Genre genre){
        return artistRepository.save(MusicFactoryIT.artist(genre));
    }

    public void createArtists(Genre genre, String namePrefix){
        for (int i = 0; i < totalElements; i++) {
            artistRepository.save(new Artist(namePrefix + i, genre));
        }
    }
}
