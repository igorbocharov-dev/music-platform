package org.musicplatform.music.exception.auth;

import org.musicplatform.music.error.auth.VerificationTokenErrorCode;

public class VerifyEmailTokenException extends RuntimeException {

    private final VerificationTokenErrorCode errorCode;

    public VerifyEmailTokenException(VerificationTokenErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public VerificationTokenErrorCode getErrorCode(){
        return this.errorCode;
    }
}
