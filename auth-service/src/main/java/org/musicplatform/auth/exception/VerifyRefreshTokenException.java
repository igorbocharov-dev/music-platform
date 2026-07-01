package org.musicplatform.auth.exception;

import org.musicplatform.auth.error.RefreshTokenErrorCode;

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
