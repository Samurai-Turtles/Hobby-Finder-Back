package com.hobbyFinder.hubby.controller;

import com.hobbyFinder.hubby.controller.routes.UserRoutes;
import com.hobbyFinder.hubby.exception.AuthException.Login.CredenciaisLoginException;
import com.hobbyFinder.hubby.exception.AuthException.Registro.CredenciaisRegistroException;
import com.hobbyFinder.hubby.exception.HubbyException;
import com.hobbyFinder.hubby.exception.NotFound.UserNotFoundException;
import com.hobbyFinder.hubby.models.dto.participations.GetResponseParticipationsUser;
import com.hobbyFinder.hubby.models.dto.participations.ParticipationDto;
import com.hobbyFinder.hubby.models.dto.participations.UpdateParticipationDto;
import com.hobbyFinder.hubby.models.dto.user.*;
import com.hobbyFinder.hubby.models.enums.UserParticipation;
import com.hobbyFinder.hubby.services.IServices.AuthInterface;
import com.hobbyFinder.hubby.services.IServices.ParticipationInterface;
import com.hobbyFinder.hubby.services.IServices.UserInterface;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private AuthInterface authInterface;

    @Autowired
    private UserInterface userInterface;

    //pode haver refatoracao do endpoint a seguir se for decidido que haverá user e person
    @GetMapping(UserRoutes.GET_USER_BY_ID)
    public ResponseEntity<UserResponseDTO> getUser(@RequestParam UUID id) throws UserNotFoundException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userInterface.getUserResponse(id));
    }

    @PostMapping(UserRoutes.LOGIN)
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthDTO request) throws CredenciaisLoginException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authInterface.loginUsuario(request));
    }

    @PostMapping(UserRoutes.POST_USER)
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDTO request) throws CredenciaisRegistroException {
        authInterface.registroUsuario(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping(UserRoutes.PUT_AUTH_USER)
    public ResponseEntity<UserDTO> put(@RequestBody @Valid UserPutDTO userPutDto) throws HubbyException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userInterface.updateUser(userPutDto));
    }

    @PostMapping(UserRoutes.LOGOUT)
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        authInterface.logoutUsuario(request);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @DeleteMapping(UserRoutes.DELETE)
    public ResponseEntity<Void> delete()  {
        userInterface.deleteUser();
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @DeleteMapping(UserRoutes.RECOVER_PASSOWRD)
    public ResponseEntity<Void> recoverPassword() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Não implementado!");
    }

}
