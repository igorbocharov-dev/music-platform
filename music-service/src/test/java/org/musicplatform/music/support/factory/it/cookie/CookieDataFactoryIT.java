package org.musicplatform.music.support.factory.it.cookie;

import jakarta.servlet.http.Cookie;
import org.musicplatform.music.security.cookie.CookieProperties;


public class CookieDataFactoryIT {

    public static Cookie cookie(CookieProperties properties, Integer maxAge, String cookieValue){
        Cookie cookie = new Cookie(properties.getRefreshTokenName(), cookieValue);
        cookie.setPath(properties.getPath());
        cookie.setSecure(properties.getSecure());
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(properties.getHttpOnly());
        return cookie;
    }
}
