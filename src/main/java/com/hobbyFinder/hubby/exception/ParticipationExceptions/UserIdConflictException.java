package com.hobbyFinder.hubby.exception.ParticipationExceptions;

import com.hobbyFinder.hubby.exception.HubbyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserIdConflictException extends HubbyException {
    public UserIdConflictException() {
        super(ParticipationExceptionsMessages.SELF_DELETE_ID_DENIED);
    }
}
