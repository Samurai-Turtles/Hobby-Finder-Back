package com.hobbyFinder.hubby.services.ServicesImpl;

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username);
    }

    @Override
    public void RegistroUsuario(RegisterDTO request) {
        validaRegistro(request);
        String encryptedPassword = new BCryptPasswordEncoder().encode(request.password());
        User newUser = new User(request.login(), encryptedPassword, request.role());
        this.userRepository.save(newUser);
    }

    @Override
    public LoginResponseDTO LoginUsuario(AuthDTO request) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.login(), request.password());
        var auth = this.authManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return new LoginResponseDTO(token);
    }

    private void validaRegistro(RegisterDTO request) throws UsuarioJaExisteException {
        if(this.userRepository.findByLogin(request.login()) != null) {
            throw new UsuarioJaExisteException();
        }
    }
}