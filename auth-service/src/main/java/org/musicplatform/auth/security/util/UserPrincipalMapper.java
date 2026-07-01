package org.musicplatform.auth.security.util;

import org.musicplatform.auth.entity.User;
import org.musicplatform.auth.security.userDetails.UserPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public final class UserPrincipalMapper {

    public static UserPrincipal from(User user){
        return new UserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.isAccountNonLocked(),
                user.isEnabled(),
                List.of(new SimpleGrantedAuthority(user.getRole().getAuthority())));
    }
}
