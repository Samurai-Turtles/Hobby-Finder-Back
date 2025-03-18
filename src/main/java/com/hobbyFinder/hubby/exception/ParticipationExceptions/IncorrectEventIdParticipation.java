package com.hobbyFinder.hubby.exception.ParticipationExceptions;

import com.hobbyFinder.hubby.exception.HubbyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class IncorrectEventIdParticipation extends HubbyException {
    public IncorrectEventIdParticipation() {
        super(ParticipationExceptionsMessages.INCORRECT_EVENT_ID);
    }
}
