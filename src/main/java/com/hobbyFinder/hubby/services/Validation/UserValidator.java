package com.hobbyFinder.hubby.services.Validation;

import com.hobbyFinder.hubby.exception.AuthException.Registro.*;
import com.hobbyFinder.hubby.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserValidator {


    @Autowired
    private UserRepository userRepository;

    private static final String USERNAME_PATTERN = ".*[^a-zA-Z0-9_.].*";
    private static final String EMAIl_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";


    public void validaUsername(String username) throws CredenciaisRegistroException {
        if (username.length() < 4)
            throw new UsernameTamanhoInvalidoException();

        if (username.matches(USERNAME_PATTERN))
            throw new UsernameInvalidoException();

        if (this.userRepository.findByUsername(username) != null)
            throw new UsuarioJaExisteException();
    }

    public void validaEmail(String email) throws CredenciaisRegistroException {
        if(!Pattern.matches(EMAIl_PATTERN, email))
            throw new EmailInvalidoException();

        if(this.userRepository.findByEmail(email).isPresent())
            throw new EmailJaRegistradoException();
    }

    public void validaPassword(String password) throws CredenciaisRegistroException {
        if(password.length() < 8)
            throw new SenhaTamanhoInvalidoException();
    }
}
