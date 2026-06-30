package org.musicplatform.music.support.fixture.jpa;

import org.musicplatform.music.entity.genre.Genre;
import org.musicplatform.music.entity.music.Album;
import org.musicplatform.music.entity.music.Artist;
import org.musicplatform.music.entity.music.Sound;
import org.musicplatform.music.support.factory.it.music.MusicFactoryIT;
import org.musicplatform.music.support.fixture.integration.SoundAggregate;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.musicplatform.music.support.assertions.PageAssertions.totalElements;

public class SoundJpaFixture {

    public static SoundAggregate soundAggregateWithSounds(Genre genre, TestEntityManager entityManager, String soundTitlePrefix, String endKeyName) {
        Artist artist = entityManager.persist(MusicFactoryIT.artist(genre));
        Album album = entityManager.persist(MusicFactoryIT.album(artist, genre));
        album.setImage(entityManager.persist(MusicFactoryIT.albumImage(album)));
        List<Sound> sounds = new ArrayList<>();
        for (int i = 0; i < totalElements; i++) {
            sounds.add(entityManager.persist
                    (new Sound(soundTitlePrefix + "_" + i,
                            265, artist, album, "sound_" + i + endKeyName,
                            LocalDate.of(2012, 6, 19), genre)));
        }
        return new SoundAggregate(artist, album, sounds);
    }

    public static SoundAggregate soundAggregateWithOneSound(Genre genre, TestEntityManager entityManager){
        Artist artist = entityManager.persist(MusicFactoryIT.artist(genre));
        Album album = entityManager.persist(MusicFactoryIT.album(artist, genre));
        album.setImage(entityManager.persist(MusicFactoryIT.albumImage(album)));
        Sound sound = entityManager.persist(MusicFactoryIT.sound(artist, album, genre));
        return new SoundAggregate(artist, album, List.of(sound));
    }
}
