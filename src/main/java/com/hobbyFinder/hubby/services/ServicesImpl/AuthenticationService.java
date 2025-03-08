package com.hobbyFinder.hubby.services.ServicesImpl;

import com.hobbyFinder.hubby.exception.AuthException.Login.*;
import com.hobbyFinder.hubby.exception.AuthException.Registro.*;
import com.hobbyFinder.hubby.repositories.TokenBlacklistRepository;
import com.hobbyFinder.hubby.models.dto.user.AuthDTO;
import com.hobbyFinder.hubby.models.dto.user.LoginResponseDTO;
import com.hobbyFinder.hubby.models.dto.user.RegisterDTO;
import com.hobbyFinder.hubby.models.entities.RevokedToken;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.repositories.UserRepository;
import com.hobbyFinder.hubby.services.IServices.AuthInterface;
import jakarta.servlet.http.HttpServletRequest;
import com.hobbyFinder.hubby.services.Validation.UserValidatorCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.hobbyFinder.hubby.infra.security.TokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService, AuthInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;

    @Lazy
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserValidatorCreate userValidatorCreate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    @Override
    public void registroUsuario(RegisterDTO request) throws CredenciaisRegistroException {
        userValidatorCreate.validaRegistro(request);
        String encryptedPassword = new BCryptPasswordEncoder().encode(request.password());
        User newUser = new User(request.email(), request.username(), encryptedPassword, request.role());
        this.userRepository.save(newUser);
    }

    @Override
    public LoginResponseDTO loginUsuario(AuthDTO request) throws CredenciaisLoginException {
        validaLogin(request);
        UserDetails user = loadUserByUsername(getUsernameFromLogin(request.login()));
        var usernamePassword = new UsernamePasswordAuthenticationToken(user.getUsername(), request.password());
        var auth = this.authManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return new LoginResponseDTO(token);
    }

    @Override
    public void logoutUsuario(HttpServletRequest request) {
        var token = recoverToken(request);

        if (token != null) {
            tokenBlacklistRepository.save(new RevokedToken(token));
        }
    }

    private void validaRegistro(RegisterDTO request) throws CredenciaisRegistroException {
        validaRegistroPassword(request);
        validaRegistroUsername(request);
        validaRegistroEmail(request);
    }

    private void validaRegistroUsername(RegisterDTO request) throws CredenciaisRegistroException {
        if (request.username() == null)
            throw new RegistroCampoNuloException();

        if (request.username().length() < 4)
            throw new UsernameTamanhoInvalidoException();

        if (request.username().matches(USERNAME_PATTERN))
            throw new UsernameInvalidoException();

        if (this.userRepository.findByUsername(request.username()) != null)
            throw new UsuarioJaExisteException();
    }

    private void validaRegistroEmail(RegisterDTO request) throws CredenciaisRegistroException {
        if(request.email() == null || request.email().trim().isEmpty())
            throw new EmailInvalidoException();

        if(!Pattern.matches(EMAIl_PATTERN, request.email()))
            throw new EmailInvalidoException();

        if(this.userRepository.findByEmail(request.email()).isPresent())
            throw new EmailJaRegistradoException();
    }

    private void validaRegistroPassword(RegisterDTO request) throws CredenciaisRegistroException {
        if(request.password() == null)
            throw new RegistroCampoNuloException();

        if(request.password().length() < 8)
            throw new SenhaTamanhoInvalidoException();
    }

    private String getUsernameFromLogin(String login) {
        return (!login.contains("@")) ? login : this.userRepository.findByEmail(login)
                .map(User::getUsername)
                .orElse(null);
    }

    private void validaLogin(AuthDTO request) throws CredenciaisLoginException {
        String username = getUsernameFromLogin(request.login());

        if (username == null || !this.userRepository.existsByUsername(username))
            throw new TentativaLoginIncorretaException();
    }

    // Método já existe em securityFilter, passivo de refatoração
    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}