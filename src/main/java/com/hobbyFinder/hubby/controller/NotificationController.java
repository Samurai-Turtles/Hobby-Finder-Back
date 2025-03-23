package com.hobbyFinder.hubby.controller;

import java.util.List;
import java.util.UUID;

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

  private final NotificationService notificationService;

  @Autowired
  public NotificationController(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  /**
   * Obtém as notificações de um usuário específico.
   *
   * @param userId ID do usuário cujas notificações devem ser recuperadas.
   * @param qtdNotificacaoPerPagina Número de notificações por página.
   * @param pagina Número da página a ser recuperada.
   * @return Lista de notificações do usuário.
   */
  @GetMapping("/{userId}")
  public ResponseEntity<List<Notification>> getNotifications(
    @PathVariable UUID userId,
    @RequestParam(defaultValue = "20") int qtdNotificacaoPerPagina,
    @RequestParam(defaultValue = "0") int pagina
  ) {
    // Validação dos parâmetros
    if (qtdNotificacaoPerPagina <= 0) {
      return ResponseEntity.badRequest().body(null); // Retorna 400 se a quantidade for inválida
    }
    if (pagina < 0) {
      return ResponseEntity.badRequest().body(null); // Retorna 400 se a página for negativa
    }

    List<Notification> notifications = notificationService.getNotificationsByUser(
      userId,
      qtdNotificacaoPerPagina,
      pagina
    );

    // Verifica se a lista de notificações está vazia e se a página é maior que 0
    if (notifications.isEmpty() && pagina > 0) {
      return ResponseEntity.status(404).body(null); // Retorna 404 se a página não existir
    }

    return ResponseEntity.ok(notifications);
  }
}
