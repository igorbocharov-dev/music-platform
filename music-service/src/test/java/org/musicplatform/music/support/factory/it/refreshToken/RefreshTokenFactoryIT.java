package org.musicplatform.music.support.factory.it.refreshToken;

import org.musicplatform.music.entity.auth.RefreshToken;
import org.musicplatform.music.entity.user.User;
import org.musicplatform.music.security.properties.RefreshTokenProperties;
import org.musicplatform.music.security.refreshToken.RefreshTokenCryptoService;
import org.musicplatform.music.security.refreshToken.RefreshTokenRepository;

import java.time.Duration;
import java.time.Instant;

public class RefreshTokenFactoryIT {

    public static RefreshToken refreshToken(String refreshTokenValue, User user, RefreshTokenProperties refreshTokenProperties, RefreshTokenCryptoService refreshTokenCryptoService, RefreshTokenRepository refreshTokenRepository){
        String hash = refreshTokenCryptoService.hash(refreshTokenValue);
        Duration refreshTokenDuration = refreshTokenProperties.getDuration();
        return refreshTokenRepository.save(new RefreshToken(hash, Instant.now().plus(refreshTokenDuration), user.getId()));
    }
}
