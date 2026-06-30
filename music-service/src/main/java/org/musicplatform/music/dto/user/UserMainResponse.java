package org.musicplatform.music.dto.user;

import org.musicplatform.music.dto.image.ImageResponse;

public record UserMainResponse(String username, ImageResponse avatar) {}