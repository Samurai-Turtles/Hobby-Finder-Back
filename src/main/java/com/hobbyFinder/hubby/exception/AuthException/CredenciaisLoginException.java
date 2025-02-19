package com.hobbyFinder.hubby.exception.AuthException;

import com.hobbyFinder.hubby.exception.HubbyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CredenciaisLoginException extends HubbyException {
    public CredenciaisLoginException(String message) {
        super(message);
    }
}
