package org.musicplatform.music.exception.handler;

import org.musicplatform.music.error.ApiErrorResponse;
import org.musicplatform.music.exception.storage.UploadObjectStorageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class S3StorageHandler {

    @ExceptionHandler(UploadObjectStorageException.class)
    public ResponseEntity<ApiErrorResponse> uploadObjectStorageHandler(UploadObjectStorageException e){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
                new ApiErrorResponse("UPLOAD_DATA_ERROR", e.getMessage(),
                        status.value(), System.currentTimeMillis(), null));
    }
}
