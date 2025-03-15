package com.hobbyFinder.hubby.exception.AuthException.Login;

import com.hobbyFinder.hubby.exception.AuthException.AuthExceptionsMessages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = AuthExceptionsMessages.INVALID_LOGIN_CREDENTIALS)
public class TentativaLoginIncorretaException extends CredenciaisLoginException {
    public TentativaLoginIncorretaException() {
        super(AuthExceptionsMessages.INVALID_LOGIN_CREDENTIALS, HttpStatus.UNAUTHORIZED);
    }
}
