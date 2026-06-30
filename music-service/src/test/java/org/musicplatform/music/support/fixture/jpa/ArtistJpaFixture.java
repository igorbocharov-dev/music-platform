package org.musicplatform.music.support.fixture.jpa;

import org.musicplatform.music.entity.genre.Genre;
import org.musicplatform.music.entity.music.Artist;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.musicplatform.music.support.assertions.PageAssertions.totalElements;

public class ArtistJpaFixture {

    public static void createArtists(Genre genre, TestEntityManager entityManager, String artistNamePrefix){
        for (int i = 0; i < totalElements; i++) {
            entityManager.persist(new Artist(artistNamePrefix + "_" + i, genre));
        }
    }
}
