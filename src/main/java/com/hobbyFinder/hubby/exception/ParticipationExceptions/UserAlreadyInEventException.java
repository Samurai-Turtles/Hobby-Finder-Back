package com.hobbyFinder.hubby.exception.ParticipationExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class UserAlreadyInEventException extends RuntimeException {
    public UserAlreadyInEventException() {
        super(ParticipationExceptionsMessages.USER_ALREADY_PARTICIPATE);
    }
}
