package org.musicplatform.music.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.musicplatform.music.security.properties.JwtTokenProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class JwtTokenService {

    private final JWTVerifier jwtVerifier;

    @Autowired
    public JwtTokenService(JwtTokenProperties jwtTokenProperties) {
        Algorithm algorithm = Algorithm.HMAC256(jwtTokenProperties.getJwtSecretKey());
        this.jwtVerifier = JWT.require(algorithm)
                .withIssuer(jwtTokenProperties.getIssuer())
                .acceptLeeway(jwtTokenProperties.getLeewaySeconds())
                .build();
    }

    public DecodedJWT validateToken(String token) throws JWTVerificationException {
        return jwtVerifier.verify(token);
    }

}
