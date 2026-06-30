package org.musicplatform.music.service.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.musicplatform.music.dto.user.LoginRequest;
import org.musicplatform.music.dto.user.RegistrationRequest;
import org.musicplatform.music.entity.auth.RefreshToken;
import org.musicplatform.music.entity.user.User;
import org.musicplatform.music.security.dto.TokenResponse;
import org.musicplatform.music.security.dto.TokenSubject;
import org.musicplatform.music.security.dto.VerifyEmailRequest;
import org.musicplatform.music.security.jwt.JwtTokenService;
import org.musicplatform.music.security.refreshToken.RefreshTokenService;
import org.musicplatform.music.security.userDetails.UserDetailsServiceImpl;
import org.musicplatform.music.security.userDetails.UserPrincipal;
import org.musicplatform.music.security.verificationToken.VerificationTokenService;
import org.musicplatform.music.service.user.UserAvatarService;
import org.musicplatform.music.service.user.UserService;
import org.musicplatform.music.service.validator.RegistrationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserAvatarService avatarService;
    private final RegistrationValidator registrationValidator;
    private final RefreshTokenService refreshTokenService;
    private final VerificationTokenService verificationTokenService;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtTokenService jwtTokenService;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, UserService userService, UserAvatarService avatarService, RegistrationValidator registrationValidator, RefreshTokenService refreshTokenService, VerificationTokenService verificationTokenService, UserDetailsServiceImpl userDetailsService, JwtTokenService jwtTokenService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.avatarService = avatarService;
        this.registrationValidator = registrationValidator;
        this.refreshTokenService = refreshTokenService;
        this.verificationTokenService = verificationTokenService;
        this.userDetailsService = userDetailsService;
        this.jwtTokenService = jwtTokenService;
    }

    @Transactional
    public TokenResponse processRegistration(RegistrationRequest regRequest, MultipartFile file, HttpServletResponse response){
        registrationValidator.validateUsername(regRequest.getUsername());
        registrationValidator.validateEmail(regRequest.getEmail());
        User newUser = userService.create(regRequest);
        Long userId = newUser.getId();
        avatarService.create(file, newUser);
        verificationTokenService.createToken(new VerifyEmailRequest(userId, newUser.getEmail()));
        refreshTokenService.create(userId, response);
        String accessToken = jwtTokenService.generateToken(new TokenSubject(userId, List.of(newUser.getRole().getAuthority())));
        return new TokenResponse(accessToken);
    }

    @Transactional
    public TokenResponse processLogin(LoginRequest loginRequest, HttpServletResponse response){
        Authentication authentication = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Long userId = principal.userId();
        refreshTokenService.deleteByUserId(userId, response);
        refreshTokenService.create(userId, response);
        TokenSubject subject =  new TokenSubject(userId, principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        String accessToken = jwtTokenService.generateToken(subject);
        return new TokenResponse(accessToken);
    }

    @Transactional
    public TokenResponse refreshAccess(HttpServletResponse response, HttpServletRequest request){
        RefreshToken foundToken = refreshTokenService.verifyRequest(request);
        refreshTokenService.rotation(foundToken, response);
        Long userId = foundToken.getUserId();
        UserPrincipal principal = userDetailsService.loadPrincipalById(userId);
        TokenSubject tokenSubject =  new TokenSubject(userId, principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        String accessToken = jwtTokenService.generateToken(tokenSubject);
        return new TokenResponse(accessToken);
    }

}
