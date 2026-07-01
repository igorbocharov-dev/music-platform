package org.musicplatform.auth.support.factory.unit.auth;


import org.musicplatform.auth.dto.VerifyEmailRequest;

public class VerificationTokenFactory {

    public static VerifyEmailRequest verifyEmailRequest(){
        return new VerifyEmailRequest(1L, "test@mail.com");
    }
}
