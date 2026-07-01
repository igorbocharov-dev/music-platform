package org.musicplatform.auth.exception;


import org.musicplatform.auth.error.UniqueFieldErrorCode;

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
