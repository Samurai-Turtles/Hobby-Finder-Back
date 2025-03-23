package com.hobbyFinder.hubby.services.ServicesImpl;

import java.util.List;
import java.util.UUID;

import com.hobbyFinder.hubby.models.entities.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hobbyFinder.hubby.exception.NotFound.EventNotFoundException;
import com.hobbyFinder.hubby.exception.NotFound.UserNotFoundException;
import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.models.entities.Notification;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.repositories.NotificationRepository;
import com.hobbyFinder.hubby.repositories.UserRepository;

@Service
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;
  private final EventRepository eventRepository;

  @Autowired
  public NotificationService(
    NotificationRepository notificationRepository,
    UserRepository userRepository,
    EventRepository eventRepository
  ) {
    this.notificationRepository = notificationRepository;
    this.userRepository = userRepository;
    this.eventRepository = eventRepository;
  }

  public void notifyRequestRejected(UUID userId, UUID eventId) {
    User user = findUserById(userId);
    Event event = findEventById(eventId);
    String message = String.format(
      "Sua solicitação para participar do evento '%s' foi rejeitada.",
      event.getName()
    );
    sendNotification(user, message, user.getPhoto());
  }

  public void notifyRequestAccepted(UUID userId, UUID eventId) {
    User user = findUserById(userId);
    Event event = findEventById(eventId);
    String message = String.format(
      "Você foi aceito no evento '%s'.",
      event.getName()
    );
    sendNotification(user, message, user.getPhoto());
  }

  public void notifyEventCancelled(UUID userId, UUID eventId) {
    User user = findUserById(userId);
    Event event = findEventById(eventId);
    String message = String.format(
      "Sua solicitação para participar do evento '%s' foi cancelada.",
      event.getName()
    );
    sendNotification(user, message, user.getPhoto());
  }

  public void notifyUserRemoved(UUID userId, UUID eventId) {
    User user = findUserById(userId);
    Event event = findEventById(eventId);
    String message = String.format(
      "Você foi removido do evento '%s' pelo organizador.",
      event.getName()
    );
    sendNotification(user, message, user.getPhoto());
  }

  public void notifyNewParticipantsConfirmed(UUID userId, UUID eventId) {
    User user = findUserById(userId);
    Event event = findEventById(eventId);
    String message = String.format(
      "Novos participantes foram confirmados no evento '%s'.",
      event.getName()
    );
    sendNotification(user, message, user.getPhoto());
  }

  public void notifyNewParticipationRequest(
    UUID organizerId,
    UUID eventId,
    String requesterUsername
  ) {
    User organizer = findUserById(organizerId);
    Event event = findEventById(eventId);
    String message = String.format(
      "O usuário '%s' deseja participar do evento '%s'.",
      requesterUsername,
      event.getName()
    );
    sendNotification(organizer, message, organizer.getPhoto());
  }

  private void sendNotification(User user, String message, Photo photo) {
    Notification notification = new Notification(user, message, photo);
    notificationRepository.save(notification);
  }

  // Métodos auxiliares para encontrar usuário e evento
  private User findUserById(UUID userId) {
    return userRepository
      .findById(userId)
      .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));
  }

  private Event findEventById(UUID eventId) {
    return eventRepository
      .findById(eventId)
      .orElseThrow(() -> new EventNotFoundException("Evento não encontrado."));
  }

}
