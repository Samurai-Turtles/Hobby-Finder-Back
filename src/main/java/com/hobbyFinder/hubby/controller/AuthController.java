package com.hobbyFinder.hubby.controller;

import com.hobbyFinder.hubby.exception.AuthException.Login.CredenciaisLoginException;
import com.hobbyFinder.hubby.exception.AuthException.Registro.CredenciaisRegistroException;
import com.hobbyFinder.hubby.models.dto.user.AuthDTO;
import com.hobbyFinder.hubby.models.dto.user.LoginResponseDTO;
import com.hobbyFinder.hubby.models.dto.user.RegisterDTO;
import com.hobbyFinder.hubby.models.entities.CustomPrincipal;
import com.hobbyFinder.hubby.services.IServices.AuthInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthInterface authInterface;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody AuthDTO request) throws CredenciaisLoginException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authInterface.loginUsuario(request));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterDTO request) throws CredenciaisRegistroException {
        authInterface.registroUsuario(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/tutorial")
    //TODO: EXCLUA ESSE MÉTODO APÓS TUTORIAL
    public ResponseEntity<String> testeTutorial() {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal customPrincipal = (CustomPrincipal) authUser.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(customPrincipal.email());
    }
}