package com.hobbyFinder.hubby.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hobbyFinder.hubby.controller.routes.UserRoutes;
import com.hobbyFinder.hubby.exception.HubbyException;
import com.hobbyFinder.hubby.exception.AuthException.Login.CredenciaisLoginException;
import com.hobbyFinder.hubby.exception.AuthException.Registro.CredenciaisRegistroException;
import com.hobbyFinder.hubby.exception.NotFound.UserNotFoundException;
import com.hobbyFinder.hubby.models.dto.user.AuthDTO;
import com.hobbyFinder.hubby.models.dto.user.LoginResponseDTO;
import com.hobbyFinder.hubby.models.dto.user.RegisterDTO;
import com.hobbyFinder.hubby.models.dto.user.UserDTO;
import com.hobbyFinder.hubby.models.dto.user.UserPutDTO;
import com.hobbyFinder.hubby.models.dto.user.UserResponseDTO;
import com.hobbyFinder.hubby.services.IServices.AuthInterface;
import com.hobbyFinder.hubby.services.IServices.UserInterface;
import com.hobbyFinder.hubby.services.ServicesImpl.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private AuthInterface authInterface;

    @Autowired
    private UserInterface userInterface;

    @Autowired
    private UserService userService;

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
    public ResponseEntity<Void> recoverPassword(@RequestBody @Valid String email) {
        userService.recoverPassword(email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
