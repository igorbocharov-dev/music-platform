package org.musicplatform.music.service.validator;

import org.musicplatform.music.error.user.UniqueFieldErrorCode;
import org.musicplatform.music.exception.user.RegistrationException;
import org.musicplatform.music.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RegistrationValidator {

    private final UserRepository userRepository;

    @Autowired
    public RegistrationValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateUsername(String username){
        if(userRepository.existsByUsername(username)){
            throw new RegistrationException("Пользователь с таким именем уже существует", UniqueFieldErrorCode.USERNAME);
        }
    }

    public void validateEmail(String email){
        if(userRepository.existsByEmail(email)){
            throw new RegistrationException("Пользователь с таким email уже существует", UniqueFieldErrorCode.EMAIL);
        }
    }
}
