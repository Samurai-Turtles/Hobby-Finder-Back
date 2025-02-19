package com.hobbyFinder.hubby.exception.AuthException.Login;

import com.hobbyFinder.hubby.exception.AuthException.AuthExceptionsMessages;
import com.hobbyFinder.hubby.exception.HubbyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = AuthExceptionsMessages.INVALID_LOGIN_CREDENTIALS)
public class TentativaLoginIncorretaException extends HubbyException {
    public TentativaLoginIncorretaException() {
        super(AuthExceptionsMessages.INVALID_LOGIN_CREDENTIALS);
    }
}
