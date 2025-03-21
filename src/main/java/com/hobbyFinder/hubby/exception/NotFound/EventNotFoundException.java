package com.hobbyFinder.hubby.exception.NotFound;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Evento não encontrado.")
public class EventNotFoundException extends NotFoundException {

    public EventNotFoundException(String message) {
        super(message);
    }
}
