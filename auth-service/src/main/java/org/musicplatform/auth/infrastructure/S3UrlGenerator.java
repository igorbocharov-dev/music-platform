package org.musicplatform.auth.infrastructure;

public final class S3UrlGenerator {
    public static String generatePublicUrl(String bucket, String key) {
        return String.format("https://%s.storage.yandexcloud.net/%s", bucket, key);
    }
}
