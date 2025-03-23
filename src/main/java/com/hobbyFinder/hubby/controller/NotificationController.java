package com.hobbyFinder.hubby.controller;

import java.util.List;
import java.util.UUID;

import com.hobbyFinder.hubby.services.IServices.NotificationInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hobbyFinder.hubby.models.entities.Notification;
import com.hobbyFinder.hubby.services.ServicesImpl.NotificationService;

@RestController
@RequestMapping("/api/notificacao")
public class NotificationController {

  private final NotificationInterface notificationService;

  @Autowired
  public NotificationController(NotificationInterface notificationService) {
    this.notificationService = notificationService;
  }



}
