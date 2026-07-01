package org.musicplatform.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.musicplatform.auth.dto.TokenResponse;
import org.musicplatform.auth.dto.user.LoginRequest;
import org.musicplatform.auth.dto.user.RegistrationRequest;
import org.musicplatform.auth.service.refreshToken.RefreshTokenService;
import org.musicplatform.auth.service.verificationToken.VerificationTokenService;
import org.musicplatform.auth.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;
    private final VerificationTokenService verificationTokenService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, RefreshTokenService refreshTokenService, VerificationTokenService verificationTokenService) {
        this.authenticationService = authenticationService;
        this.refreshTokenService = refreshTokenService;
        this.verificationTokenService = verificationTokenService;
    }

    @PostMapping(value = "/registration", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TokenResponse> registration(@Valid @RequestPart("user") RegistrationRequest regRequest,
                                                      @RequestPart(name = "file", required = false) MultipartFile file,
                                                      HttpServletResponse response){
        return ResponseEntity.ok(authenticationService.processRegistration(regRequest, file, response));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest,
                                               HttpServletResponse response) {
            return ResponseEntity.ok(authenticationService.processLogin(loginRequest, response));
    }

    @PostMapping("/logout")
    public ResponseEntity<HttpStatus> logout(HttpServletRequest request, HttpServletResponse response){
        refreshTokenService.dropToken(request, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(HttpServletResponse response, HttpServletRequest request){
        return ResponseEntity.ok(authenticationService.refreshAccess(response, request));
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activate(@RequestParam(required = false, value = "token") String token){
        return ResponseEntity.ok(verificationTokenService.verify(token));
    }

}
