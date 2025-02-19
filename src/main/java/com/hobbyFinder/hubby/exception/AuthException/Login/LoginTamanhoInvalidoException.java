package com.hobbyFinder.hubby.exception.AuthException.Login;

import com.hobbyFinder.hubby.exception.AuthException.AuthExceptionsMessages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = AuthExceptionsMessages.INVALID_REGISTER_LOGIN_SIZE)
public class LoginTamanhoInvalidoException extends CredenciaisLoginException {
    public LoginTamanhoInvalidoException() {
        super(AuthExceptionsMessages.INVALID_REGISTER_LOGIN_SIZE);
    }
}
