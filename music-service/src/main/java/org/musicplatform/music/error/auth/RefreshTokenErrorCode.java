package org.musicplatform.music.error.auth;

public enum RefreshTokenErrorCode {

    MISSING("Refresh token is missing"), INVALID("Refresh token invalid");

    private final String message;

    RefreshTokenErrorCode(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
