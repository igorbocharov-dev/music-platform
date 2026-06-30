package org.musicplatform.music.support.fixture.integration;

import org.musicplatform.music.entity.music.Album;
import org.musicplatform.music.entity.music.Artist;
import org.musicplatform.music.entity.music.Sound;

import java.util.List;

public record SoundAggregate(Artist artist, Album album, List<Sound> sounds) {}