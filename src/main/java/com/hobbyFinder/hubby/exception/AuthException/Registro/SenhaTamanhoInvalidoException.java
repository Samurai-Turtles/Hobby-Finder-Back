package com.hobbyFinder.hubby.exception.AuthException.Registro;

import com.hobbyFinder.hubby.exception.AuthException.AuthExceptionsMessages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = AuthExceptionsMessages.INVALID_REGISTER_PASSWORD_SIZE)
public class SenhaTamanhoInvalidoException extends CredenciaisRegistroException {
    public SenhaTamanhoInvalidoException() {
        super(AuthExceptionsMessages.INVALID_REGISTER_PASSWORD_SIZE, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
