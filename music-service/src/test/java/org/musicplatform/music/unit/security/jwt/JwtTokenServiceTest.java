package org.musicplatform.music.unit.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.musicplatform.music.security.jwt.JwtTokenService;
import org.musicplatform.music.security.properties.JwtTokenProperties;
import org.musicplatform.music.support.factory.unit.auth.JwtTokenFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtTokenServiceTest {

    private JwtTokenService jwtTokenService;

    @BeforeEach
    void setupJwtProperties(){
        JwtTokenProperties jwtTokenProperties = mock(JwtTokenProperties.class);
        when(jwtTokenProperties.getJwtSecretKey()).thenReturn(JwtTokenFactory.secret());
        when(jwtTokenProperties.getIssuer()).thenReturn(JwtTokenFactory.issuer());
        when(jwtTokenProperties.getLeewaySeconds()).thenReturn(JwtTokenFactory.leewaySeconds());
        jwtTokenService = new JwtTokenService(jwtTokenProperties);
    }

    @Test
    void validateToken_ShouldReturnDecodedJWT_whenTokenValid(){
        Algorithm algorithm = Algorithm.HMAC256(JwtTokenFactory.secret());

        String token = JWT.create()
                .withIssuer(JwtTokenFactory.issuer())
                .withSubject("1")
                .withArrayClaim("roles", new String[]{"ROLE_USER"})
                .withExpiresAt(Date.from(Instant.now().plus(Duration.ofMinutes(10))))
                .sign(algorithm);

        DecodedJWT decodedJWT = jwtTokenService.validateToken(token);

        assertEquals("1", decodedJWT.getSubject());
    }

    @Test
    void validateToken_shouldThrow_whenTokenInvalid(){
        String invalidToken = "invalid";

        assertThrows(JWTVerificationException.class, ()-> jwtTokenService.validateToken(invalidToken));
    }

}
