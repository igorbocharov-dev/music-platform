package org.musicplatform.auth.dto;

public record VerifyEmailRequest (Long userId, String email) {
}
