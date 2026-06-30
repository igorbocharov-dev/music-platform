package org.musicplatform.music.dto.music.sound;

import org.musicplatform.music.dto.music.album.AlbumInfo;
import org.musicplatform.music.dto.music.artist.ArtistResponse;

import java.time.LocalDate;

public record SoundPageResponse(
        Long id, String title, Integer duration, String key, String url,
        LocalDate releaseDate, ArtistResponse artist, AlbumInfo album) {}