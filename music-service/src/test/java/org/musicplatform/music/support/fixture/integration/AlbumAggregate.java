package org.musicplatform.music.support.fixture.integration;

import org.musicplatform.music.entity.music.Album;
import org.musicplatform.music.entity.music.Artist;

import java.util.List;

public record AlbumAggregate(Artist artist, List<Album> albums) {}