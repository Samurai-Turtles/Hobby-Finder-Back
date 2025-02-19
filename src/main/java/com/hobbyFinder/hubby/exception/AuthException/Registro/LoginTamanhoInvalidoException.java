package com.hobbyFinder.hubby.exception.AuthException.Registro;

import com.hobbyFinder.hubby.exception.AuthException.AuthExceptionsMessages;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = AuthExceptionsMessages.INVALID_REGISTER_LOGIN_SIZE)
public class LoginTamanhoInvalidoException extends CredenciaisRegistroException {
    public LoginTamanhoInvalidoException() {
        super(AuthExceptionsMessages.INVALID_REGISTER_LOGIN_SIZE);
    }
}
