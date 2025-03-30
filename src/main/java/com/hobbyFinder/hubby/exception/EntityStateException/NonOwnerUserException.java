package com.hobbyFinder.hubby.exception.EntityStateException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hobbyFinder.hubby.exception.HubbyException;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class NonOwnerUserException extends HubbyException {
    public NonOwnerUserException(String message) {
        super(message);
    }

}
