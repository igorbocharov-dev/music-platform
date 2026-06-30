package org.musicplatform.music.exception.auth;

import org.musicplatform.music.error.auth.RefreshTokenErrorCode;

public class VerifyRefreshTokenException extends RuntimeException {

    private final RefreshTokenErrorCode code;

    public VerifyRefreshTokenException(RefreshTokenErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public RefreshTokenErrorCode getCode(){
        return code;
    }
}
