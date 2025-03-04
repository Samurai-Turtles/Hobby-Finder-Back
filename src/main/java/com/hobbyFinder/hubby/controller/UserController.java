package com.hobbyFinder.hubby.controller;

import com.hobbyFinder.hubby.controller.routes.EventRoutes;
import com.hobbyFinder.hubby.controller.routes.UserRoutes;
import com.hobbyFinder.hubby.exception.AuthException.Login.CredenciaisLoginException;
import com.hobbyFinder.hubby.exception.AuthException.Registro.CredenciaisRegistroException;
import com.hobbyFinder.hubby.models.dto.events.EventCreateDto;
import com.hobbyFinder.hubby.models.dto.events.EventDto;
import com.hobbyFinder.hubby.models.dto.user.*;
import com.hobbyFinder.hubby.models.entities.CustomPrincipal;
import com.hobbyFinder.hubby.services.IServices.AuthInterface;
import com.hobbyFinder.hubby.services.IServices.UserInterface;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(UserRoutes.BASE)
public class UserController {

    @Autowired
    private AuthInterface authInterface;

    @Autowired
    private UserInterface userInterface;

    //pode haver refatoracao do endpoint a seguir se for decidido que haverá user e person
    @GetMapping(UserRoutes.GET_USER_BY_ID)
    public ResponseEntity<UserDTO> getUser(@RequestParam UUID id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userInterface.getUser(id));
    }

    @PostMapping(UserRoutes.LOGIN)
    public ResponseEntity<LoginResponseDTO> login(@RequestBody AuthDTO request) throws CredenciaisLoginException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authInterface.loginUsuario(request));
    }

    @PostMapping(UserRoutes.POST_USER)
    public ResponseEntity<Void> register(@RequestBody RegisterDTO request) throws CredenciaisRegistroException {
        authInterface.registroUsuario(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping(UserRoutes.PUT_AUTH_USER)
    public ResponseEntity<Void> put(@RequestBody UserPutDTO userPutDto) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Não implementado!");
    }

    @PutMapping(UserRoutes.LOGOUT)
    public ResponseEntity<Void> logout() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Não implementado!");
    }

    @DeleteMapping(UserRoutes.DELETE)
    public ResponseEntity<Void> delete() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Não implementado!");
    }

    @DeleteMapping(UserRoutes.RECOVER_PASSOWRD)
    public ResponseEntity<Void> recoverPassword() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Não implementado!");
    }

    @GetMapping("/tutorial")
    //TODO: EXCLUA ESSE MÉTODO APÓS TUTORIAL
    public ResponseEntity<String> testeTutorial() {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal customPrincipal = (CustomPrincipal) authUser.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(customPrincipal.email());
    }
}
