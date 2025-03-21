package com.hobbyFinder.hubby.exception.EntityStateException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EventIsPublicException extends RuntimeException {
    public EventIsPublicException(String message) {
        super(message);
    }
}
