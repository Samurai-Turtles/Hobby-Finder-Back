package com.hobbyFinder.hubby.services.ServicesImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.hobbyFinder.hubby.models.dto.notifications.NotificationDto;
import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.models.entities.Notification;
import com.hobbyFinder.hubby.models.entities.Participation;
import com.hobbyFinder.hubby.models.entities.Photo;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.models.enums.UserRole;
import com.hobbyFinder.hubby.repositories.NotificationRepository;
import com.hobbyFinder.hubby.services.IServices.UserInterface;
import com.hobbyFinder.hubby.util.GetUserLogged;

@ContextConfiguration(classes = { NotificationService.class })
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
public class NotificationServiceTest {

  @MockBean
  private GetUserLogged getUserLogged;

  @MockBean
  private NotificationRepository notificationRepository;

  @Autowired
  private NotificationService notificationService;

  @MockBean
  private UserInterface userInterface;

  @Test
  @DisplayName("Test notifyChangeEvent; should notify all participants")
  void testNotifyChangeEvent_shouldNotifyAllParticipants() {
    // Arrange
    Event event = mock(Event.class);
    User user1 = new User();
    user1.setId(UUID.randomUUID());
    User user2 = new User();
    user2.setId(UUID.randomUUID());
    Participation participation1 = new Participation();
    participation1.setIdUser(user1.getId());
    Participation participation2 = new Participation();
    participation2.setIdUser(user2.getId());
    List<Participation> participations = List.of(
      participation1,
      participation2
    );
    when(event.getParticipations()).thenReturn(participations);
    when(userInterface.getUser(user1.getId())).thenReturn(user1);
    when(userInterface.getUser(user2.getId())).thenReturn(user2);

    // Act
    notificationService.notifyChangeEvent(event);

    // Assert
    verify(notificationRepository, times(2)).save(any(Notification.class));
  }

  @Test
  @DisplayName("Test getNotifications(Pageable); then return toList Empty")
  void testGetNotifications_thenReturnToListEmpty() {
    // Arrange
    when(
      notificationRepository.findByUserId(
        Mockito.<UUID>any(),
        Mockito.<Pageable>any()
      )
    )
      .thenReturn(new PageImpl<>(new ArrayList<>()));

    Photo photo = new Photo();
    photo.setExtension("Extension");
    photo.setId(UUID.randomUUID());
    photo.setSaved(true);

    User user = new User();
    user.setBio("Bio");
    user.setEmail("jane.doe@example.org");
    user.setFullName("Dr Jane Doe");
    user.setId(UUID.randomUUID());
    user.setInterests(new ArrayList<>());
    user.setNotifications(new ArrayList<>());
    user.setParticipations(new ArrayList<>());
    user.setPassword("iloveyou");
    user.setPhoto(photo);
    user.setRequests(new ArrayList<>());
    user.setRole(UserRole.ADMIN);
    user.setStars(10.0d);
    user.setUsername("janedoe");
    when(getUserLogged.getUserLogged()).thenReturn(user);

    // Act
    Page<NotificationDto> actualNotifications = notificationService.getNotifications(
      null
    );

    // Assert
    verify(notificationRepository).findByUserId(isA(UUID.class), isNull());
    verify(getUserLogged).getUserLogged();
    assertTrue(actualNotifications instanceof PageImpl);
    assertTrue(actualNotifications.toList().isEmpty());
  }

  @Test
  @DisplayName("Test notifyAproveSolicitation; should notify user")
  void testNotifyAproveSolicitation_shouldNotifyUser() {
    // Arrange
    User user = new User();
    user.setId(UUID.randomUUID());
    Event event = new Event();
    event.setName("Sample Event");

    // Act
    notificationService.notifyAproveSolicitation(user, event);

    // Assert
    verify(notificationRepository).save(any(Notification.class));
  }
}
