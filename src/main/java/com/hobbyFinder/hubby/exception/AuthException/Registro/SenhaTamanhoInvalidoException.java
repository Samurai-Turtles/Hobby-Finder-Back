package com.hobbyFinder.hubby.exception.AuthException.Registro;

import com.hobbyFinder.hubby.exception.AuthException.AuthExceptionsMessages;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = AuthExceptionsMessages.INVALID_REGISTER_PASSWORD_SIZE)
public class SenhaTamanhoInvalidoException extends CredenciaisRegistroException {
    public SenhaTamanhoInvalidoException() {
        super(AuthExceptionsMessages.INVALID_REGISTER_PASSWORD_SIZE);
    }
}
