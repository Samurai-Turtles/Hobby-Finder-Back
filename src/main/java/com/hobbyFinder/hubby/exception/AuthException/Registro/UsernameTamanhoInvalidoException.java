package com.hobbyFinder.hubby.exception.AuthException.Registro;

import com.hobbyFinder.hubby.exception.AuthException.AuthExceptionsMessages;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = AuthExceptionsMessages.INVALID_REGISTER_USERNAME_SIZE)
public class UsernameTamanhoInvalidoException extends CredenciaisRegistroException {
    public UsernameTamanhoInvalidoException() {
        super(AuthExceptionsMessages.INVALID_REGISTER_USERNAME_SIZE);
    }
}
