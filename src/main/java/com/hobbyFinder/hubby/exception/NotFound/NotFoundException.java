package com.hobbyFinder.hubby.exception.NotFound;

import com.hobbyFinder.hubby.exception.HubbyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends HubbyException {
  public NotFoundException(String message) {
    super(message);
  }
}
