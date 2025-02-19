package com.hobbyFinder.hubby.exception.AuthException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = AuthExceptionsMessages.INVALID_REGISTER_PASSWORD_CREDENTIAL_SIZE)
public class SenhaTamanhoInvalidoException extends CredenciaisLoginException {
    public SenhaTamanhoInvalidoException() {
        super(AuthExceptionsMessages.INVALID_REGISTER_PASSWORD_CREDENTIAL_SIZE);
    }
}
