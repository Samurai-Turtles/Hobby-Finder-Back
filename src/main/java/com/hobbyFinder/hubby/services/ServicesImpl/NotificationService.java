package com.hobbyFinder.hubby.services.ServicesImpl;

import java.util.List;
import java.util.UUID;

import com.hobbyFinder.hubby.models.entities.*;
import com.hobbyFinder.hubby.models.enums.NotificationEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hobbyFinder.hubby.models.dto.notifications.NotificationDto;
import com.hobbyFinder.hubby.models.dto.photos.PhotoDto;
import com.hobbyFinder.hubby.models.dto.user.UserResponseDTO;
import com.hobbyFinder.hubby.repositories.NotificationRepository;
import com.hobbyFinder.hubby.services.IServices.NotificationInterface;
import com.hobbyFinder.hubby.services.IServices.UserInterface;
import com.hobbyFinder.hubby.util.GetUserLogged;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@AllArgsConstructor
public class NotificationService implements NotificationInterface {

  private NotificationRepository notificationRepository;
  private UserInterface userRepository;
  private GetUserLogged getUserLogged;

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
      userTurn = userRepository.getUser(participation.getIdUser());
      message = String.format(NOTIFY_CHANGE_EVENT, event.getName());
      postNotification(userTurn, photo, message, event.getId(), NotificationEnum.CHANGE_EVENT);
    }
  }

  @Override
  public void notifySolicitation(User user, Event event, ParticipationRequest request) {
    notifyOrganizer(event, NOTIFY_SOLICITATION, user, request.getId(), NotificationEnum.ORGANIZER_SOLICITATION);
  }

  @Override
  public void notifyAproveSolicitation(User user, Event event) {
    String message = String.format(NOTIFY_APROVE, event.getName());

    postNotification(user, event.getPhoto(), message, user.getId(), NotificationEnum.CLIENT_SOLICITATION);
  }

  @Override
  public void notifyConfirmParticipation(User user, Event event, Participation participation) {
    notifyOrganizer(event, NOTIFY_CONFIRM, user, participation.getIdParticipation(), NotificationEnum.PARTICIPATION);
  }

  @Override
  public Page<NotificationDto> getNotifications(Pageable pageable) {
    User user = getUserLogged.getUserLogged();
    Page<Notification> notifications = notificationRepository.findByUserId(user.getId(), pageable);

    return notifications.map(this::createNotificationDto);
  }

  private NotificationDto createNotificationDto(Notification notification) {
    User user = notification.getUser();

    Photo photo = notification.getPhoto();
    PhotoDto photoDto = new PhotoDto(photo.getId(), photo.getExtension(), photo.isSaved());

    UserResponseDTO userDTO = new UserResponseDTO(
            user.getId(), user.getUsername(), user.getFullName(),
            user.getBio(), user.getInterests(), photoDto, user.getStars());

    NotificationDto notificationDto = new NotificationDto(
            notification.getId(), notification.getMessage(), photoDto,
            userDTO, notification.getDate(), notification.getIdNotification(), notification.getType());

    return notificationDto;
  }

  private void notifyOrganizer(Event event, String constant, User user, UUID id, NotificationEnum type) {
    User organizer;
    Photo photoUser = user.getPhoto();
    for (Participation participation : event.getParticipations()) {
      if (!participation.isOrganizerParticipation())
        continue;

      organizer = userRepository.getUser(participation.getIdUser());
      String message = String.format(constant, user.getUsername(),event.getName());
      postNotification(organizer, photoUser, message, id, type);
    }
  }

  public void postNotification(User user, Photo photo, String message, UUID idNotification, NotificationEnum type) {
    Notification notification = new Notification(user, message, photo, idNotification, type);

    notificationRepository.save(notification);
  }
}
