package com.hobbyFinder.hubby.services.Validation;

import com.hobbyFinder.hubby.exception.AuthException.Registro.CredenciaisRegistroException;
import com.hobbyFinder.hubby.exception.AuthException.Registro.RegistroCampoNuloException;
import com.hobbyFinder.hubby.models.dto.user.RegisterDTO;
import org.springframework.stereotype.Service;

@Service
public class UserValidatorCreate extends UserValidatorBase{

    public void validaRegistro(RegisterDTO request) throws CredenciaisRegistroException {
        if (request.username() == null)
            throw new RegistroCampoNuloException();
        super.validaUsername(request.username());

        if (request.email() == null || request.email().trim().isEmpty())
            throw new RegistroCampoNuloException();
        super.validaEmail(request.email());

        if (request.password() == null)
            throw new RegistroCampoNuloException();
        super.validaPassword(request.password());
    }
}
