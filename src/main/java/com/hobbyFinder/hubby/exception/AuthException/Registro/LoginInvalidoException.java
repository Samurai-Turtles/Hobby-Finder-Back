package com.hobbyFinder.hubby.exception.AuthException.Registro;

import com.hobbyFinder.hubby.exception.AuthException.AuthExceptionsMessages;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = AuthExceptionsMessages.INVALID_REGISTER_CREDENTIALS)
public class LoginInvalidoException extends CredenciaisRegistroException {
    public LoginInvalidoException() {
        super(AuthExceptionsMessages.INVALID_REGISTER_CREDENTIALS);
    }
}
