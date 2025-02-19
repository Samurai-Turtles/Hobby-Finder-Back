package com.hobbyFinder.hubby.services.ServicesImpl;

import com.hobbyFinder.hubby.exception.AuthException.Registro.CredenciaisRegistroException;
import com.hobbyFinder.hubby.exception.AuthException.Registro.SenhaTamanhoInvalidoException;
import com.hobbyFinder.hubby.exception.AuthException.Registro.UsuarioJaExisteException;
import com.hobbyFinder.hubby.infra.security.TokenService;
import com.hobbyFinder.hubby.models.dto.user.AuthDTO;
import com.hobbyFinder.hubby.models.dto.user.LoginResponseDTO;
import com.hobbyFinder.hubby.models.dto.user.RegisterDTO;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.repositories.UserRepository;
import com.hobbyFinder.hubby.services.IServices.AuthInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService, AuthInterface {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authManager;

    private static final String caracteresEspeciais = ".*[^a-zA-Z0-9_.].*";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    @Override
    public void registroUsuario(RegisterDTO request) {
        validaRegistro(request);
        String encryptedPassword = new BCryptPasswordEncoder().encode(request.password());
        User newUser = new User(request.email(), request.username(), encryptedPassword, request.role());
        this.userRepository.save(newUser);
    }

    @Override
    public LoginResponseDTO loginUsuario(AuthDTO request) {
        UserDetails user = loadUserByUsername(getUsernameFromLogin(request.login()));

        var usernamePassword = new UsernamePasswordAuthenticationToken(user.getUsername(), request.password());
        var auth = this.authManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        return new LoginResponseDTO(token);
    }

    private String getUsernameFromLogin(String login) {
        return (login.contains("@")) ?
                userRepository.findByEmail(login).getUsername() :
                login;
    }

    private void validaRegistro(RegisterDTO request) throws UsuarioJaExisteException {
        if(this.userRepository.findByUsername(request.username()) != null)
            throw new UsuarioJaExisteException();

        if (this.userRepository.findByEmail(request.email()) != null)
            // Retornar aqui exception para email ja registrado
            throw new UsuarioJaExisteException();

        if (request.password().length() < 8)
            throw new SenhaTamanhoInvalidoException();

        if (request.username().matches(caracteresEspeciais))
            // exception para caracteres especiais em username
            throw new CredenciaisRegistroException("Não é permitido caracteres especiais em username");
    }
}