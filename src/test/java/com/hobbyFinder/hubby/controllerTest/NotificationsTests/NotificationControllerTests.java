package com.hobbyFinder.hubby.controllerTest.NotificationsTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.hobbyFinder.hubby.controller.NotificationController;
import com.hobbyFinder.hubby.models.dto.notifications.NotificationDto;
import com.hobbyFinder.hubby.services.IServices.NotificationInterface;

class NotificationControllerTests {

  /**
   * Test {@link NotificationController#getNotifications(Optional, Optional)}.
   * <ul>
   *   <li>Then Body return {@link PageImpl}.</li>
   * </ul>
   * <p>
   * Method under test: {@link NotificationController#getNotifications(Optional, Optional)}
   */
  @Test
  @DisplayName(
    "Test getNotifications(Optional, Optional); then Body return PageImpl"
  )
  void testGetNotifications_thenBodyReturnPageImpl() {

    // Arrange
    NotificationInterface notificationService = mock(
      NotificationInterface.class
    );
    PageImpl<NotificationDto> pageImpl = new PageImpl<>(new ArrayList<>());
    when(notificationService.getNotifications(Mockito.<Pageable>any()))
      .thenReturn(pageImpl);
    NotificationController notificationController = new NotificationController(
      notificationService
    );
    Optional<Integer> notificationsPerPage = Optional.<Integer>of(1);
    Optional<Integer> page = Optional.<Integer>of(1);

    // Act
    ResponseEntity<Page<NotificationDto>> actualNotifications = notificationController.getNotifications(
      notificationsPerPage,
      page
    );

    // Assert
    verify(notificationService).getNotifications(isA(Pageable.class));
    Page<NotificationDto> body = actualNotifications.getBody();
    assertTrue(body instanceof PageImpl);
    HttpStatusCode statusCode = actualNotifications.getStatusCode();
    assertTrue(statusCode instanceof HttpStatus);
    assertEquals(200, actualNotifications.getStatusCodeValue());
    assertEquals(HttpStatus.OK, statusCode);
    assertTrue(body.toList().isEmpty());
    assertTrue(actualNotifications.hasBody());
    assertTrue(actualNotifications.getHeaders().isEmpty());
    assertSame(pageImpl, body);
  }
}
