package org.musicplatform.auth.support.factory.it.refreshToken;


import org.musicplatform.auth.entity.RefreshToken;
import org.musicplatform.auth.entity.User;
import org.musicplatform.auth.repository.RefreshTokenRepository;
import org.musicplatform.auth.security.properties.RefreshTokenProperties;
import org.musicplatform.auth.service.refreshToken.RefreshTokenCryptoService;

import java.time.Duration;
import java.time.Instant;

public class RefreshTokenFactoryIT {

    public static RefreshToken refreshToken(String refreshTokenValue, User user, RefreshTokenProperties refreshTokenProperties, RefreshTokenCryptoService refreshTokenCryptoService, RefreshTokenRepository refreshTokenRepository){
        String hash = refreshTokenCryptoService.hash(refreshTokenValue);
        Duration refreshTokenDuration = refreshTokenProperties.getDuration();
        return refreshTokenRepository.save(new RefreshToken(hash, Instant.now().plus(refreshTokenDuration), user.getId()));
    }
}
