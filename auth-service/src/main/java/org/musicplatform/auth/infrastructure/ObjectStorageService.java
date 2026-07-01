package org.musicplatform.auth.infrastructure;

public interface ObjectStorageService {
    void upload(String s3Key, String bucket, UploadS3Object uploadS3Object);
}
