package com.hobbyFinder.hubby.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TagInvalidaException extends HubbyException {
    public TagInvalidaException(String message) {
        super(message);
    }
}
