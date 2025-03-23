package com.hobbyFinder.hubby.exception.EventException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class EventCrowdedException extends RuntimeException{
    public EventCrowdedException() {
        super(EventExceptionsMessages.EVENT_CROWDED);
    }
}
