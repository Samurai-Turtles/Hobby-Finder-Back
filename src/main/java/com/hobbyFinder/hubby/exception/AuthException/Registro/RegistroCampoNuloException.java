package com.hobbyFinder.hubby.exception.AuthException.Registro;

import com.hobbyFinder.hubby.exception.AuthException.AuthExceptionsMessages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = AuthExceptionsMessages.INVALID_FIELD)
public class RegistroCampoNuloException extends CredenciaisRegistroException {
    public RegistroCampoNuloException() {
        super(AuthExceptionsMessages.INVALID_FIELD, HttpStatus.BAD_REQUEST);
    }
}
