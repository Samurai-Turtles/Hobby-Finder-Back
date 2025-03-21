package com.hobbyFinder.hubby.exception.EntityStateException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class NonOwnerUserException extends RuntimeException {
    public NonOwnerUserException(String message) {
        super(message);
    }

}
