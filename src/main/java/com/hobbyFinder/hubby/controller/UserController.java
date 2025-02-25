package com.hobbyFinder.hubby.controller;

import com.hobbyFinder.hubby.models.dto.user.UserDTO;
import com.hobbyFinder.hubby.services.IServices.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserInterface userInterface;

    //pode haver refatoracao do endpoint a seguir se for decidido que haverá user e person
    @GetMapping("/get")
    public ResponseEntity<UserDTO> getUser(@RequestParam UUID userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userInterface.getUser(userId));
    }
}
