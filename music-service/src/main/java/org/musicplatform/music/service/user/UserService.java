package org.musicplatform.music.service.user;

import org.musicplatform.music.dto.image.ImageResponse;
import org.musicplatform.music.dto.user.RegistrationRequest;
import org.musicplatform.music.dto.user.UserMainResponse;
import org.musicplatform.music.entity.image.UserAvatar;
import org.musicplatform.music.entity.user.User;
import org.musicplatform.music.exception.user.UserNotFoundException;
import org.musicplatform.music.mapper.image.ImageMapper;
import org.musicplatform.music.repository.image.UserAvatarRepository;
import org.musicplatform.music.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserAvatarRepository userAvatarRepository;
    private final ImageMapper imageMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserAvatarRepository userAvatarRepository, ImageMapper imageMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userAvatarRepository = userAvatarRepository;
        this.imageMapper = imageMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User create(RegistrationRequest regRequest){
        String password = passwordEncoder.encode(regRequest.getPassword());
        User user = new User(regRequest.getUsername(), password, regRequest.getEmail(), regRequest.getDateOfBirth());
        return userRepository.save(user);
    }

    public UserMainResponse mainResponse(Long userId){
        String username = getUsernameById(userId);
        UserAvatar userAvatar = userAvatarRepository.findByUserId(userId);
        ImageResponse imageResponse = imageMapper.toImageResponse(userAvatar);
        return new UserMainResponse(username, imageResponse);
    }

    public String getUsernameById(Long userId){
        return userRepository.getUsernameById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

}
