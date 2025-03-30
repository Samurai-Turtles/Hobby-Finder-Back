package com.hobbyFinder.hubby.exception.NotFound;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "Pagina não encontrada.")
public class PageNotExistException extends NotFoundException {
    public PageNotExistException(String message) {
        super(message);
    }
}
