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
        User newUser = new User(request.email(), request.username(), encryptedPassword, request.nomeCompleto());
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