package com.hobbyFinder.hubby.exception;

import com.hobbyFinder.hubby.exception.AuthException.AuthExceptionsMessages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = AuthExceptionsMessages.INVALID_INTEREST)
public class TagInvalidaException extends HubbyException {
    public TagInvalidaException() {
        super(AuthExceptionsMessages.INVALID_INTEREST);
    }
}
