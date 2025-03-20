package com.hobbyFinder.hubby.exception.EntityStateException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PageIsEmptyException extends RuntimeException {
    public PageIsEmptyException(String message) {
        super(message);
    }
}
