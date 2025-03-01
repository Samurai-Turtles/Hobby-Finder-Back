package com.hobbyFinder.hubby.services.Validation;

import com.hobbyFinder.hubby.exception.HubbyException;
import com.hobbyFinder.hubby.models.dto.user.UserPutDTO;
import org.springframework.stereotype.Service;

@Service
public class UserValidatorUpdate extends UserValidatorBase{

    public void validaUpdate(UserPutDTO request) throws HubbyException {
        if (request.username() != null)
            super.validaUsername(request.username());

        if (request.password() != null)
            super.validaPassword(request.password());

        if (request.email() != null)
            super.validaEmail(request.email());
    }
}
