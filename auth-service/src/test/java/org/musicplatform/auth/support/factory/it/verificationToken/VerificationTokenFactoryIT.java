package org.musicplatform.auth.support.factory.it.verificationToken;


import org.musicplatform.auth.entity.User;
import org.musicplatform.auth.entity.VerificationToken;
import org.musicplatform.auth.security.properties.VerificationTokenProperties;

import java.time.Instant;
import java.util.UUID;

public class VerificationTokenFactoryIT {

    public static VerificationToken validVerificationToken(User user, VerificationTokenProperties properties){
        String token = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plus(properties.getExpirationHours());
        return new VerificationToken(user, token, expiryDate);
    }

    public static VerificationToken expiredVerificationToken(User user){
        String token = UUID.randomUUID().toString();
        Instant expired = Instant.now().minusSeconds(60);
        return new VerificationToken(user, token, expired);
    }
}
