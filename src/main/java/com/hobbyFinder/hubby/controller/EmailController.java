package com.hobbyFinder.hubby.controller;

import com.hobbyFinder.hubby.models.dto.email.EmailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.hobbyFinder.hubby.services.ServicesImpl.EmailService;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void enviarEmail(@RequestBody EmailDto emailDto) {
        emailService.enviaEmail(emailDto);

    }
}