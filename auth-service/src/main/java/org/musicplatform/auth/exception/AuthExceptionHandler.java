package org.musicplatform.auth.exception;

import org.musicplatform.auth.error.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(VerifyRefreshTokenException.class)
    public ResponseEntity<ApiErrorResponse> handleVerifyRefreshTokenException(VerifyRefreshTokenException e){
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(
                new ApiErrorResponse(e.getCode().name(), e.getMessage(), status.value(),
                        System.currentTimeMillis(), null));
    }

    @ExceptionHandler(VerifyEmailTokenException.class)
    public ResponseEntity<ApiErrorResponse> handleVerifyEmailTokenException(VerifyEmailTokenException e){
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(
                new ApiErrorResponse(e.getErrorCode().name(), e.getMessage(), status.value(),
                        System.currentTimeMillis(), null));
    }
}
