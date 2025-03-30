package com.hobbyFinder.hubby.exception.AuthException.Registro;

import com.hobbyFinder.hubby.exception.AuthException.AuthExceptionsMessages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = AuthExceptionsMessages.USER_ALREADY_REGISTERED)
public class UsuarioJaExisteException extends CredenciaisRegistroException {
    public UsuarioJaExisteException() {
        super(AuthExceptionsMessages.USER_ALREADY_REGISTERED, HttpStatus.CONFLICT);
    }
}
