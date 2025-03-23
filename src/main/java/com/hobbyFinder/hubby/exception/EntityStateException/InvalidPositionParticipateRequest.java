package com.hobbyFinder.hubby.exception.EntityStateException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hobbyFinder.hubby.exception.HubbyException;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.ParticipationExceptionsMessages;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class InvalidPositionParticipateRequest extends HubbyException {
    public InvalidPositionParticipateRequest() {
        super(ParticipationExceptionsMessages.INVALID_USER_POSITION);
    }
}
