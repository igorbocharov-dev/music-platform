package org.musicplatform.music.support.factory.unit.auth;

import org.musicplatform.music.security.dto.VerifyEmailRequest;

public class VerificationTokenFactory {

    public static VerifyEmailRequest verifyEmailRequest(){
        return new VerifyEmailRequest(1L, "test@mail.com");
    }
}
