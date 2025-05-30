package com.hobbyFinder.hubby.exception.EventException;

import com.hobbyFinder.hubby.exception.HubbyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UserNotEventPermissionException extends HubbyException {
    public UserNotEventPermissionException() {super(EventExceptionsMessages.USER_NOT_PERMISSION);}
}
