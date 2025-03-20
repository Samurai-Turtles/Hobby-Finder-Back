package com.hobbyFinder.hubby.services.ServicesImpl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.models.entities.Notification;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.repositories.NotificationRepository;

@Service
public class NotificationService {

  private final NotificationRepository notificationRepository;

  @Autowired
  public NotificationService(NotificationRepository notificationRepository) {
    this.notificationRepository = notificationRepository;
  }

  public List<Notification> getNotificationsByUser(
    UUID userId,
    int qtdNotificacaoPerPagina,
    int pagina
  ) {
    Pageable pageable = PageRequest.of(pagina, qtdNotificacaoPerPagina);
    return notificationRepository.findByUser_Id(userId, pageable);
  }

  public void createNotification(String message, User user) {
    Notification notification = new Notification(message, user);
    notificationRepository.save(notification);
  }

  // Métodos adicionais para gerar notificações conforme os requisitos
  public void notifyEventChange(Event event) {
    // Implementação para notificar mudanças em eventos
  }

  public void notifyParticipationRequest(Event event, User requester) {
    // Implementação para notificar solicitações de participação
  }

  public void notifyApproval(User requester, Event event) {
    // Implementação para notificar aprovações de participação
  }

  public void notifyPresenceConfirmation(Event event, User participant) {
    // Implementação para notificar confirmações de presença
  }
}
