package com.hobbyFinder.hubby.exception.EntityStateException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hobbyFinder.hubby.exception.HubbyException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EventIsPublicException extends HubbyException {
    public EventIsPublicException(String message) {
        super(message);
    }
}
