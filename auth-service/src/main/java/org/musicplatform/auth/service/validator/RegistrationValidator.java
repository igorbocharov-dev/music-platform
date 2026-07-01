package org.musicplatform.auth.service.validator;

import org.musicplatform.auth.error.UniqueFieldErrorCode;
import org.musicplatform.auth.exception.RegistrationException;
import org.musicplatform.auth.repository.UserRepository;
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
