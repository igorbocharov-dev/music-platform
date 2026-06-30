package org.musicplatform.music.dto.music.album;

import org.musicplatform.music.dto.image.ImageResponse;
import org.musicplatform.music.dto.music.artist.ArtistResponse;

import java.time.LocalDate;

public record AlbumResponse(Long id, String title, LocalDate releaseDate, ImageResponse image, ArtistResponse artist) {}
