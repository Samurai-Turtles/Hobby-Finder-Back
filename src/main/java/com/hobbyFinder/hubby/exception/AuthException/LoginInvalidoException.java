package com.hobbyFinder.hubby.exception.AuthException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = AuthExceptionsMessages.INVALID_REGISTER_CREDENTIALS)
public class LoginInvalidoException extends CredenciaisLoginException{
    public LoginInvalidoException() {
        super(AuthExceptionsMessages.INVALID_REGISTER_CREDENTIALS);
    }
}
