package com.hobbyFinder.hubby.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hobbyFinder.hubby.models.entities.Notification;
import com.hobbyFinder.hubby.services.ServicesImpl.NotificationService;

@RestController
@RequestMapping("/api/notificacao")
public class NotificationController {

  private final NotificationService notificationService;

  @Autowired
  public NotificationController(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @GetMapping
  public ResponseEntity<?> getNotifications(
    @RequestParam(defaultValue = "20") int qtdNotificacaoPerPagina,
    @RequestParam(defaultValue = "0") int pagina,
    @RequestParam UUID userId
  ) {
    List<Notification> notifications = notificationService.getNotificationsByUser(
      userId,
      qtdNotificacaoPerPagina,
      pagina
    );

    if (notifications.isEmpty() && pagina > 0) {
      return ResponseEntity.status(404).body("Página não existe");
    }

    return ResponseEntity.ok(notifications);
  }
}
