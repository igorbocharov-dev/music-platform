package org.musicplatform.auth.service.user;

import org.musicplatform.auth.entity.User;
import org.musicplatform.auth.entity.UserAvatar;
import org.musicplatform.auth.infrastructure.MultipartFileUploadS3ObjectAdapter;
import org.musicplatform.auth.infrastructure.ObjectStorageService;
import org.musicplatform.auth.infrastructure.YandexStorageProperties;
import org.musicplatform.auth.repository.UserAvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class UserAvatarService {

    private final UserAvatarRepository userAvatarRepository;
    private final YandexStorageProperties yandexStorageProperties;
    private final ObjectStorageService objectStorageService;

    @Autowired
    public UserAvatarService(UserAvatarRepository userAvatarRepository, YandexStorageProperties yandexStorageProperties,
                             @Qualifier("uploadImage") ObjectStorageService objectStorageService) {
        this.userAvatarRepository = userAvatarRepository;
        this.yandexStorageProperties = yandexStorageProperties;
        this.objectStorageService = objectStorageService;
    }

    @Transactional
    public UserAvatar create(MultipartFile file, User user) {
        String s3key = generateUploadAvatarKey(file);
        MultipartFileUploadS3ObjectAdapter multipartFileUploadS3ObjectAdapter = new MultipartFileUploadS3ObjectAdapter(file);
        objectStorageService.upload(s3key, yandexStorageProperties.getBuckets().get("img"), multipartFileUploadS3ObjectAdapter);
        return userAvatarRepository.save(new UserAvatar(user, s3key));
    }

    private String generateUploadAvatarKey(MultipartFile file){
        if(file == null) return yandexStorageProperties.getDefaultAvatarKey();
        return String.format("avatar/%s", UUID.randomUUID() + "_" + file.getOriginalFilename());
    }

}
