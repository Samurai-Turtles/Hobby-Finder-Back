package com.hobbyFinder.hubby.exception.AuthException.Registro;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CredenciaisRegistroException extends RuntimeException {
    public CredenciaisRegistroException(String message) {
        super(message);
    }
}
