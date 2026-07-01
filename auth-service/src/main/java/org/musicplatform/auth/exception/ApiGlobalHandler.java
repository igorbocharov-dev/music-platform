package org.musicplatform.auth.exception;

import org.musicplatform.auth.error.ApiErrorResponse;
import org.musicplatform.auth.error.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiGlobalHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> userNotFoundHandle(UserNotFoundException e){
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(
                new ApiErrorResponse(ErrorType.USER_NOT_FOUND_ERROR.name(), e.getMessage(),
                        status.value(), System.currentTimeMillis(), null));
    }
}
