package com.hobbyFinder.hubby.services.ServicesImpl;

import java.util.List;

import com.hobbyFinder.hubby.models.entities.*;
import com.hobbyFinder.hubby.services.IServices.NotificationInterface;
import com.hobbyFinder.hubby.services.IServices.UserInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.repositories.NotificationRepository;
import com.hobbyFinder.hubby.repositories.UserRepository;

@Service
@AllArgsConstructor
public class NotificationService implements NotificationInterface {

  private NotificationRepository notificationRepository;
  private UserInterface userRepository;

  private static final String  NOTIFY_CHANGE_EVENT = "O Evento '%s' ocorreu mudanças.";
  private static final String  NOTIFY_SOLICITATION = "O Usuário '%s' quer participar do seu evento '%s'.";
  private static final String  NOTIFY_APROVE = "Você foi Aprovado no Evento '%s'.";
  private static final String  NOTIFY_CONFIRM = "O Usuário '%s' foi Aprovado no Evento '%s'.";

  @Override
  public void notifyChangeEvent(Event event) {
    List<Participation> participations = event.getParticipations();
    User userTurn;
    Photo photo = event.getPhoto();
    String message;
    for (Participation participation : participations) {
      userTurn = userRepository.getUser(participation.getIdEvent());
      message = String.format(NOTIFY_CHANGE_EVENT, event.getName());
      postNotification(userTurn, photo, message);
    }
  }

  @Override
  public void notifySolicitation(User user, Event event) {
    notifyOrganizer(event, NOTIFY_SOLICITATION, user);
  }

  @Override
  public void notifyAproveSolicitation(User user, Event event) {
    String message = String.format(NOTIFY_APROVE, event.getName());

    postNotification(user, event.getPhoto(), message);
  }

  @Override
  public void notifyConfirmParticipation(User user, Event event) {
    notifyOrganizer(event, NOTIFY_CONFIRM, user);
  }

  private void notifyOrganizer(Event event, String constant, User user) {
    User organizer;
    Photo photoUser = user.getPhoto();
    for (Participation participation : event.getParticipations()) {
      if (!participation.isOrganizerParticipation())
        continue;

      organizer = userRepository.getUser(participation.getIdUser());
      String message = String.format(constant, user.getUsername(),event.getName());
      postNotification(organizer, photoUser, message);
    }
  }

  private void postNotification(User user, Photo photo, String message) {
    Notification notification = new Notification(user, message, photo);

    notificationRepository.save(notification);
  }
}
