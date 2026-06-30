package org.musicplatform.music.security.dto;

public record VerifyEmailRequest (Long userId, String email) {
}
