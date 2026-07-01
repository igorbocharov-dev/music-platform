package org.musicplatform.auth.exception;


import org.musicplatform.auth.error.VerificationTokenErrorCode;

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
