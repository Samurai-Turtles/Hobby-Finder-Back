package com.hobbyFinder.hubby.exception.EventException;

import com.hobbyFinder.hubby.exception.HubbyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidCapacityException extends HubbyException {
  public InvalidCapacityException() {super(EventExceptionsMessages.INVALID_CAPACITY);}
}
