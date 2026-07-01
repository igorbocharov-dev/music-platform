package org.musicplatform.auth.support.factory.unit.auth;


import org.musicplatform.auth.dto.TokenSubject;
import org.musicplatform.auth.entity.Authority;

import java.util.List;

public class TokenSubjectFactory {

    public static TokenSubject tokenSubject(){
        return new TokenSubject(1L, List.of(Authority.USER.getAuthority()));
    }
}
