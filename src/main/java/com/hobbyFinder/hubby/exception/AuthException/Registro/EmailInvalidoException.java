package com.hobbyFinder.hubby.exception.AuthException.Registro;

import com.hobbyFinder.hubby.exception.AuthException.AuthExceptionsMessages;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = AuthExceptionsMessages.INVALID_REGISTER_EMAIL)
public class EmailInvalidoException extends CredenciaisRegistroException {
    public EmailInvalidoException() {
        super(AuthExceptionsMessages.INVALID_REGISTER_EMAIL);
    }
}
