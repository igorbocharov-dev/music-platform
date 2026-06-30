package org.musicplatform.music.mapper.music;

import org.mapstruct.Named;
import org.musicplatform.music.storage.s3.S3UrlGenerator;
import org.musicplatform.music.storage.s3.YandexStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SoundUrlMapper {

    private final YandexStorageProperties yandexStorageProperties;

    @Autowired
    public SoundUrlMapper(YandexStorageProperties yandexStorageProperties) {
        this.yandexStorageProperties = yandexStorageProperties;
    }

    @Named("mapMp3Url")
    public String mapUrl(String key){
        return S3UrlGenerator.generatePublicUrl(yandexStorageProperties.getBuckets().get("music"), key);
    }
}
