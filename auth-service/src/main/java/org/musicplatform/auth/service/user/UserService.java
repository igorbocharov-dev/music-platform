package org.musicplatform.auth.service.user;

import org.musicplatform.auth.dto.ImageResponse;
import org.musicplatform.auth.dto.user.RegistrationRequest;
import org.musicplatform.auth.dto.user.UserMainResponse;
import org.musicplatform.auth.entity.User;
import org.musicplatform.auth.entity.UserAvatar;
import org.musicplatform.auth.exception.UserNotFoundException;
import org.musicplatform.auth.mapper.ImageMapper;
import org.musicplatform.auth.repository.UserAvatarRepository;
import org.musicplatform.auth.repository.UserRepository;
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
