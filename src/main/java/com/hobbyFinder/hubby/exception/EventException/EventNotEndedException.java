package com.hobbyFinder.hubby.exception.EventException;

import com.hobbyFinder.hubby.exception.HubbyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = EventExceptionsMessages.EVENT_NOT_ENDEND)
public class EventNotEndedException extends HubbyException {
    public EventNotEndedException() {
        super(EventExceptionsMessages.EVENT_NOT_ENDEND);
    }
}
