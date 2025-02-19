package com.hobbyFinder.hubby.exception.AuthException.Registro;

import com.hobbyFinder.hubby.exception.AuthException.AuthExceptionsMessages;
import com.hobbyFinder.hubby.exception.AuthException.Login.CredenciaisLoginException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = AuthExceptionsMessages.INVALID_REGISTER_PASSWORD_CREDENTIAL_SIZE)
public class SenhaTamanhoInvalidoException extends CredenciaisRegistroException {
    public SenhaTamanhoInvalidoException() {
        super(AuthExceptionsMessages.INVALID_REGISTER_PASSWORD_CREDENTIAL_SIZE);
    }
}
