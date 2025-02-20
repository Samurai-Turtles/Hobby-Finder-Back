package com.hobbyFinder.hubby.exception.AuthException.Registro;

import com.hobbyFinder.hubby.exception.AuthException.AuthExceptionsMessages;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = AuthExceptionsMessages.INVALID_REGISTER_USERNAME)
public class UsernameInvalidoException extends CredenciaisRegistroException {
    public UsernameInvalidoException() {
        super(AuthExceptionsMessages.INVALID_REGISTER_USERNAME);
    }
}
