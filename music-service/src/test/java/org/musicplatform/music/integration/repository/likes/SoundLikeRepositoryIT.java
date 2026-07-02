package org.musicplatform.music.integration.repository.likes;

import org.junit.jupiter.api.Test;
import org.musicplatform.music.entity.genre.Genre;
import org.musicplatform.music.entity.genre.GenreName;
import org.musicplatform.music.entity.likes.SoundLike;
import org.musicplatform.music.entity.music.Sound;
import org.musicplatform.music.repository.likes.SoundLikeRepository;
import org.musicplatform.music.repository.music.GenreRepository;
import org.musicplatform.music.support.config.AbstractJpaIT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.musicplatform.music.support.assertions.PageAssertions.*;
import static org.musicplatform.music.support.assertions.SoundAssertions.assertSoundsWithoutRelations;
import static org.musicplatform.music.support.assertions.SoundLikeAssertions.assertSoundLikesWithSounds;
import static org.musicplatform.music.support.fixture.jpa.SoundJpaFixture.soundAggregateWithOneSound;
import static org.musicplatform.music.support.fixture.jpa.SoundJpaFixture.soundAggregateWithSounds;
import static org.musicplatform.music.support.fixture.jpa.SoundLikeJpaFixture.createSoundLikes;

public class SoundLikeRepositoryIT extends AbstractJpaIT {

    @Autowired
    private SoundLikeRepository repository;
    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Genre findGenre(){
        return genreRepository.findByName(GenreName.ROCK).orElseThrow();
    }

    @Test
    void deleteByUserIdAndSoundId_ShouldDeleteRecord() {
        Genre genre = findGenre();
        Long userId = 1L;
        Sound sound = soundAggregateWithOneSound(genre, entityManager).sounds().getFirst();
        createSoundLikes(entityManager, userId, List.of(sound));

        entityManager.flush();
        entityManager.clear();

        repository.deleteByUserIdAndSoundId(userId, sound.getId());
        entityManager.flush();
        entityManager.clear();

        List<SoundLike> soundLikes = repository.findAll();
        assertThat(soundLikes).isEmpty();
    }

    @Test
    void deleteByUserIdAndSoundId_ShouldDoNothing_WhenUserIdIsIncorrectly() {
        Genre genre = findGenre();
        Long userId = 1L;
        Sound sound = soundAggregateWithOneSound(genre, entityManager).sounds().getFirst();
        createSoundLikes(entityManager, userId, List.of(sound));

        entityManager.flush();
        entityManager.clear();

        repository.deleteByUserIdAndSoundId(9810L, sound.getId());
        entityManager.flush();
        entityManager.clear();

        List<SoundLike> soundLikes = repository.findAll();
        assertThat(soundLikes).isNotEmpty();
    }

    @Test
    void deleteByUserIdAndSoundId_ShouldDoNothing_WhenSoundIdIsIncorrectly() {
        Genre genre = findGenre();
        Long userId = 1L;
        Sound sound = soundAggregateWithOneSound(genre, entityManager).sounds().getFirst();
        createSoundLikes(entityManager, userId, List.of(sound));

        entityManager.flush();
        entityManager.clear();

        repository.deleteByUserIdAndSoundId(userId, 6512L);
        entityManager.flush();
        entityManager.clear();

        List<SoundLike> soundLikes = repository.findAll();
        assertThat(soundLikes).isNotEmpty();
    }

    @Test
    void findByUserIdOrderByCreatedAtDescIdDesc_ShouldReturnsFirstPageCorrectly() {
        Genre genre = findGenre();
        Long userId = 1L;
        String soundTitlePrefix = "bad romance";
        String endKeyName = "key";
        List<Sound> sounds = soundAggregateWithSounds(genre, entityManager,soundTitlePrefix, endKeyName).sounds();
        createSoundLikes(entityManager, userId, sounds);

        entityManager.flush();
        entityManager.clear();

        Page<SoundLike> soundLikePage = repository
                .findByUserIdOrderByCreatedAtDescIdDesc(userId, PageRequest.of(page, size));
        List<SoundLike> soundLikes = soundLikePage.getContent();

        assertSoundLikesWithSounds(soundLikes);

        List<Sound> soundsBySoundLikes = soundLikes.stream().map(SoundLike::getSound).toList();

        assertFirstPage(soundLikePage);
        assertSoundLikesOrderByCreatedAtDesc(soundLikes);
        assertSoundsWithoutRelations(soundsBySoundLikes, soundTitlePrefix, endKeyName);
    }



    @Test
    void findByUserIdOrderByCreatedAtDescIdDesc_ShouldReturnsSecondPageCorrectly() {
        Genre genre = findGenre();
        Long userId = 1L;
        String soundTitlePrefix = "starlight";
        String endKeyName = "key";
        List<Sound> sounds = soundAggregateWithSounds(genre, entityManager, soundTitlePrefix, endKeyName).sounds();
        createSoundLikes(entityManager, userId, sounds);

        entityManager.flush();
        entityManager.clear();

        Page<SoundLike> soundLikePage = repository
                .findByUserIdOrderByCreatedAtDescIdDesc(userId, PageRequest.of(page + 1, size));
        List<SoundLike> soundLikes = soundLikePage.getContent();

        assertSoundLikesWithSounds(soundLikes);

        List<Sound> soundsBySoundLikes = soundLikes.stream().map(SoundLike::getSound).toList();

        assertSecondPage(soundLikePage);
        assertSoundLikesOrderByCreatedAtDesc(soundLikes);
        assertSoundsWithoutRelations(soundsBySoundLikes, soundTitlePrefix, endKeyName);
    }

    @Test
    void findByUserIdOrderByCreatedAtDescIdDesc_ShouldReturnsLastPageCorrectly() {
        Genre genre = findGenre();
        Long userId = 1L;
        String soundTitlePrefix = "poker face";
        String endKeyName = "key";
        List<Sound> sounds = soundAggregateWithSounds(genre, entityManager, soundTitlePrefix, endKeyName).sounds();
        createSoundLikes(entityManager, userId, sounds);

        entityManager.flush();
        entityManager.clear();

        Page<SoundLike> soundLikePage = repository
                .findByUserIdOrderByCreatedAtDescIdDesc(userId, PageRequest.of(page + 2, size));
        List<SoundLike> soundLikes = soundLikePage.getContent();

        assertSoundLikesWithSounds(soundLikes);

        List<Sound> soundsBySoundLikes = soundLikes.stream().map(SoundLike::getSound).toList();

        assertLastPage(soundLikePage);
        assertSoundLikesOrderByCreatedAtDesc(soundLikes);
        assertSoundsWithoutRelations(soundsBySoundLikes, soundTitlePrefix, endKeyName);
    }

    @Test
    void findByUserIdOrderByCreatedAtDescIdDesc_ShouldReturnsEmptyPage_WhenUserIdIsInvalid() {
        Page<SoundLike> soundLikePage = repository
                .findByUserIdOrderByCreatedAtDescIdDesc(8491L, PageRequest.of(page, size));
        assertEmptyPage(soundLikePage);
    }

    @Test
    void existsByUserIdAndSoundId_ShouldReturnIsTrue_WhenSoundLikeIsExists(){
        Genre genre = findGenre();
        Long userId = 1L;
        Sound sound = soundAggregateWithOneSound(genre, entityManager).sounds().getFirst();
        createSoundLikes(entityManager, userId, List.of(sound));

        entityManager.flush();
        entityManager.clear();

        Boolean result = repository.existsByUserIdAndSoundId(userId, sound.getId());
        assertThat(result).isTrue();
    }

    @Test
    void existsByUserIdAndSoundId_ShouldReturnFalse_WhenAlbumIdIsInvalid(){
        Genre genre = findGenre();
        Long userId = 1L;
        Sound sound = soundAggregateWithOneSound(genre, entityManager).sounds().getFirst();
        createSoundLikes(entityManager, userId, List.of(sound));

        entityManager.flush();
        entityManager.clear();

        Boolean result = repository.existsByUserIdAndSoundId(userId, 8902L);
        assertThat(result).isFalse();
    }

    @Test
    void existsByUserIdAndSoundId_ShouldReturnFalse_WhenUserIdIsInvalid(){
        Genre genre = findGenre();
        Long userId = 1L;
        Sound sound = soundAggregateWithOneSound(genre, entityManager).sounds().getFirst();
        createSoundLikes(entityManager, userId, List.of(sound));

        entityManager.flush();
        entityManager.clear();

        Boolean result = repository.existsByUserIdAndSoundId(8919L, sound.getId());
        assertThat(result).isFalse();
    }

    private void assertSoundLikesOrderByCreatedAtDesc(List<SoundLike> soundLikes) {
        List<Instant> createdAtSoundLikesOrder = soundLikes.stream().map(SoundLike::getCreatedAt).toList();
        assertThat(createdAtSoundLikesOrder).isSortedAccordingTo(Comparator.reverseOrder());
    }
}