package com.hobbyFinder.hubby.exception.ParticipationExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hobbyFinder.hubby.exception.HubbyException;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class UserAlreadyInEventException extends HubbyException {
    public UserAlreadyInEventException() {
        super(ParticipationExceptionsMessages.USER_ALREADY_PARTICIPATE);
    }
}
