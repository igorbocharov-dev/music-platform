package org.musicplatform.music.support.factory.unit.auth;

import org.musicplatform.music.entity.user.Authority;
import org.musicplatform.music.security.dto.TokenSubject;

import java.util.List;

public class TokenSubjectFactory {

    public static TokenSubject tokenSubject(){
        return new TokenSubject(1L, List.of(Authority.USER.getAuthority()));
    }
}
