package org.musicplatform.music.dto.music.album;

import org.musicplatform.music.dto.image.ImageResponse;

public record AlbumInfo(Long id, String title, ImageResponse image) {}
