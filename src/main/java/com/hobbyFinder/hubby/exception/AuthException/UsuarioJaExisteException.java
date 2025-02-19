package com.hobbyFinder.hubby.exception.AuthException;

import com.hobbyFinder.hubby.exception.HubbyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = AuthExceptionsMessages.USER_ALREADY_REGISTERED)
public class UsuarioJaExisteException extends HubbyException {
    public UsuarioJaExisteException() {
        super(AuthExceptionsMessages.USER_ALREADY_REGISTERED);
    }
}
