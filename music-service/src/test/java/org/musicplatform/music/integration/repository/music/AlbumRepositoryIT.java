package org.musicplatform.music.integration.repository.music;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.junit.jupiter.api.Test;
import org.musicplatform.music.entity.genre.Genre;
import org.musicplatform.music.entity.genre.GenreName;
import org.musicplatform.music.entity.music.Album;
import org.musicplatform.music.repository.music.AlbumRepository;
import org.musicplatform.music.repository.music.GenreRepository;
import org.musicplatform.music.support.config.AbstractJpaIT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.musicplatform.music.support.assertions.AlbumAssertions.assertAlbumsWithArtistAndImage;
import static org.musicplatform.music.support.assertions.PageAssertions.*;
import static org.musicplatform.music.support.fixture.jpa.AlbumJpaFixture.albumAggregateWithAlbums;
import static org.musicplatform.music.support.fixture.jpa.AlbumJpaFixture.albumAggregateWithOneAlbum;

public class AlbumRepositoryIT extends AbstractJpaIT {

    @Autowired
    private AlbumRepository repository;
    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Genre findGenre(){
        return genreRepository.findByName(GenreName.ROCK).orElseThrow();
    }

    @Test
    void findByIdWithArtistAndImage_ShouldReturnsAlbumWithArtistAndImage(){
        Genre genre = findGenre();
        Album expectedAlbum = albumAggregateWithOneAlbum(genre, entityManager).albums().getFirst();

        entityManager.flush();
        entityManager.clear();

        Album actualAlbum = repository.findByIdWithArtistAndImage(expectedAlbum.getId()).orElseThrow();

        assertThat(actualAlbum.getId()).isEqualTo(expectedAlbum.getId());
        assertThat(actualAlbum.getTitle()).isEqualTo(expectedAlbum.getTitle());
        assertThat(actualAlbum.getReleaseDate()).isEqualTo(expectedAlbum.getReleaseDate());

        assertThat(actualAlbum.getArtist()).isNotNull().isNotInstanceOf(HibernateProxy.class);
        assertThat(Hibernate.isInitialized(actualAlbum.getArtist())).isTrue();

        assertThat(actualAlbum.getImage()).isNotNull().isNotInstanceOf(HibernateProxy.class);
        assertThat(Hibernate.isInitialized(actualAlbum.getImage())).isTrue();
    }

    @Test
    void findByIdWithArtistAndImage_ShouldReturnsEmpty_WhenIdIsInvalid(){
        assertThat(repository.findByIdWithArtistAndImage(156L)).isEmpty();
    }

    @Test
    void findByTitleStartingWithIgnoreCase_ShouldReturnsFirstPageCorrectly(){
        Genre genre = findGenre();
        String albumTitlePrefix = "bad romance";
        albumAggregateWithAlbums(genre, entityManager, albumTitlePrefix);

        entityManager.flush();
        entityManager.clear();

        Page<Album> albumPage = repository.findByTitleStartingWithIgnoreCase
                (albumTitlePrefix, PageRequest.of(page, size));
        List<Album> albums = albumPage.getContent();

        assertAlbumsWithArtistAndImage(albums, albumTitlePrefix);
        assertFirstPage(albumPage);
    }

    @Test
    void findByTitleStartingWithIgnoreCase_ShouldReturnsSecondPageCorrectly(){
        Genre genre = findGenre();
        String albumTitlePrefix = "just dance";
        albumAggregateWithAlbums(genre, entityManager, albumTitlePrefix);

        entityManager.flush();
        entityManager.clear();

        Page<Album> albumPage = repository.findByTitleStartingWithIgnoreCase
                        (albumTitlePrefix, PageRequest.of(page + 1, size));
        List<Album> albums = albumPage.getContent();

        assertAlbumsWithArtistAndImage(albums, albumTitlePrefix);
        assertSecondPage(albumPage);
    }

    @Test
    void findByTitleStartingWithIgnoreCase_ShouldReturnsLastPageCorrectly(){
        Genre genre = findGenre();
        String albumTitlePrefix = "after dark";
        albumAggregateWithAlbums(genre, entityManager, albumTitlePrefix);

        entityManager.flush();
        entityManager.clear();

        Page<Album> albumPage = repository.findByTitleStartingWithIgnoreCase
                (albumTitlePrefix, PageRequest.of(page + 2, size));
        List<Album> albums = albumPage.getContent();

        assertAlbumsWithArtistAndImage(albums, albumTitlePrefix);
        assertLastPage(albumPage);
    }

    @Test
    void findByTitleStartingWithIgnoreCase_ShouldReturnsEmptyPage_WhenAlbumsNotFoundByPrefix(){
        Page<Album> result = repository.findByTitleStartingWithIgnoreCase
                ("incorrectly prefix", PageRequest.of(page, size));

        assertEmptyPage(result);
    }

    @Test
    void findByGenreId_ShouldReturnsFirstPageCorrectly(){
        Genre genre = findGenre();
        String albumTitlePrefix = "supermassive";
        albumAggregateWithAlbums(genre, entityManager, albumTitlePrefix);

        entityManager.flush();
        entityManager.clear();

        Page<Album> albumPage = repository.findByGenreId(genre.getId(), PageRequest.of(page, size));
        List<Album> albums = albumPage.getContent();

        assertAlbumsWithArtistAndImage(albums, albumTitlePrefix);
        assertFirstPage(albumPage);
    }

    @Test
    void findByGenreId_ShouldReturnsSecondPageCorrectly(){
        Genre genre = findGenre();
        String albumTitlePrefix = "just dance";
        albumAggregateWithAlbums(genre, entityManager, albumTitlePrefix);

        entityManager.flush();
        entityManager.clear();

        Page<Album> albumPage = repository.findByGenreId(genre.getId(), PageRequest.of(page + 1, size));
        List<Album> albums = albumPage.getContent();

        assertAlbumsWithArtistAndImage(albums, albumTitlePrefix);
        assertSecondPage(albumPage);
    }

    @Test
    void findByGenreId_ShouldReturnsLastPageCorrectly(){
        Genre genre = findGenre();
        String albumTitlePrefix = "just";
        albumAggregateWithAlbums(genre, entityManager, albumTitlePrefix);

        entityManager.flush();
        entityManager.clear();

        Page<Album> albumPage = repository.findByGenreId(genre.getId(), PageRequest.of(page + 2, size));
        List<Album> albums = albumPage.getContent();

        assertAlbumsWithArtistAndImage(albums, albumTitlePrefix);
        assertLastPage(albumPage);
    }

    @Test
    void findByGenreId_ShouldReturnsEmptyPage_WhenGenreIdIsInvalid(){
        Page<Album> albumPage = repository.findByGenreId(893234L, PageRequest.of(page + 2, size));

        assertEmptyPage(albumPage);
    }

}
