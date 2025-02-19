package com.hobbyFinder.hubby.exception.AuthException.Login;

import com.hobbyFinder.hubby.exception.AuthException.AuthExceptionsMessages;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = AuthExceptionsMessages.INVALID_LOGIN_CREDENTIALS)
public class TentativaLoginIncorretaException extends CredenciaisLoginException {
    public TentativaLoginIncorretaException() {
        super(AuthExceptionsMessages.INVALID_LOGIN_CREDENTIALS);
    }
}
