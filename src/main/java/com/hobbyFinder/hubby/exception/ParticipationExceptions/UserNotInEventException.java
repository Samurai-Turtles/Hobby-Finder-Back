package com.hobbyFinder.hubby.exception.ParticipationExceptions;

import com.hobbyFinder.hubby.exception.HubbyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UserNotInEventException extends HubbyException {
    public UserNotInEventException() {
        super(ParticipationExceptionsMessages.USER_REQUISITION_DENIED);
    }
}
