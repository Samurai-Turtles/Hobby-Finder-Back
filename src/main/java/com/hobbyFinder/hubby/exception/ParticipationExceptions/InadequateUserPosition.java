package com.hobbyFinder.hubby.exception.ParticipationExceptions;

import com.hobbyFinder.hubby.exception.HubbyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class InadequateUserPosition extends HubbyException {
    public InadequateUserPosition() {
        super(ParticipationExceptionsMessages.USER_POSITION_DENIED);
    }
}
