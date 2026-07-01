package org.musicplatform.auth.unit.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.musicplatform.auth.infrastructure.YandexStorageProperties;
import org.musicplatform.auth.mapper.ImageUrlMapper;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ImageUrlMapperTest {

    @Mock
    private YandexStorageProperties yandexStorageProperties;

    @InjectMocks
    private ImageUrlMapper imageUrlMapper;

    @Test
    void mapUrl_ShouldGenerateValidPublicUrl(){
        String bucketKey = "img";
        Map<String, String> imgBucket = Map.of(bucketKey, "mus-app-img");
        String imgKey = "test_image.jpg";
        String url = String.format("https://%s.storage.yandexcloud.net/%s", imgBucket.get(bucketKey), imgKey);
        when(yandexStorageProperties.getBuckets()).thenReturn(imgBucket);

        String result = imageUrlMapper.mapUrl(imgKey);

        assertEquals(url, result);
        verify(yandexStorageProperties).getBuckets();
    }
}
