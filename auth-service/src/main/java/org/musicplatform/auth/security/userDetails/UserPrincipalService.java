package org.musicplatform.auth.security.userDetails;

public interface UserPrincipalService {
    UserPrincipal loadPrincipalById(Long id);
}
