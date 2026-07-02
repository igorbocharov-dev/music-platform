package org.musicplatform.music.support.fixture.jpa;

import org.musicplatform.music.entity.likes.SoundLike;
import org.musicplatform.music.entity.music.Sound;
import org.musicplatform.music.support.factory.it.MusicFactoryIT;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Instant;
import java.util.List;

public class SoundLikeJpaFixture {

    public static void createSoundLikes(TestEntityManager entityManager, Long userId, List<Sound> sounds) {
        Instant createdAt = Instant.parse("2026-01-01T00:00:00Z");
        int second = 1;
        for (Sound sound : sounds) {
            SoundLike soundLike = entityManager.persist(MusicFactoryIT.soundLike(userId, sound));
            soundLike.setCreatedAt(createdAt.plusSeconds(second++));
        }
    }
}
