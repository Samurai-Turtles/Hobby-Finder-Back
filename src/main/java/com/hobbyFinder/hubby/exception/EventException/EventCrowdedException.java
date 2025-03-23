package com.hobbyFinder.hubby.exception.EventException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hobbyFinder.hubby.exception.HubbyException;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class EventCrowdedException extends HubbyException{
    public EventCrowdedException() {
        super(EventExceptionsMessages.EVENT_CROWDED);
    }
}
