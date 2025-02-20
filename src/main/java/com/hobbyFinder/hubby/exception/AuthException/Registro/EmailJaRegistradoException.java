package com.hobbyFinder.hubby.exception.AuthException.Registro;

import com.hobbyFinder.hubby.exception.AuthException.AuthExceptionsMessages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = AuthExceptionsMessages.INVALID_REGISTER_EMAIL)
public class EmailJaRegistradoException extends CredenciaisRegistroException {
    public EmailJaRegistradoException() {
        super(AuthExceptionsMessages.EMAIL_ALREADY_REGISTERED);
    }
}
