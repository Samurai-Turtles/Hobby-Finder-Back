package com.hobbyFinder.hubby.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hobbyFinder.hubby.models.entities.Email;
import com.hobbyFinder.hubby.services.ServicesImpl.EmailService;

@RestController
@RequestMapping("email")
public class EmailController {

  private final EmailService emailService;

  public EmailController(EmailService emailService) {
    this.emailService = emailService;
  }

  @PostMapping
  public void sendEmail(@RequestBody Email email) {
    emailService.sendEmail(email);
  }
}
