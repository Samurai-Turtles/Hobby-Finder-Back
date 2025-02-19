package com.hobbyFinder.hubby.exception.AuthException;

import com.hobbyFinder.hubby.exception.HubbyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = AuthExceptionsMessages.INVALID_LOGIN_CREDENTIALS)
public class TentativaLoginIncorretaException extends HubbyException {
    public TentativaLoginIncorretaException() {
        super(AuthExceptionsMessages.INVALID_LOGIN_CREDENTIALS);
    }
}
