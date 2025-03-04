package com.hobbyFinder.hubby.exception.AuthException.Registro;

import com.hobbyFinder.hubby.exception.AuthException.AuthExceptionsMessages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = AuthExceptionsMessages.INVALID_REGISTER_USERNAME_SIZE)
public class UsernameTamanhoInvalidoException extends CredenciaisRegistroException {
    public UsernameTamanhoInvalidoException() {
        super(AuthExceptionsMessages.INVALID_REGISTER_USERNAME_SIZE, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
