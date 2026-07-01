package org.musicplatform.auth.dto.user;

import org.musicplatform.auth.dto.ImageResponse;

public record UserMainResponse(String username, ImageResponse avatar) {}