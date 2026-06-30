package org.musicplatform.music.exception.user;

import org.musicplatform.music.error.user.UniqueFieldErrorCode;

public class RegistrationException extends RuntimeException{

    private final UniqueFieldErrorCode code;

    public RegistrationException (String message, UniqueFieldErrorCode code){
        super(message);
        this.code = code;
    }

    public UniqueFieldErrorCode getCode(){
        return code;
    }
}
