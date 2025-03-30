package com.hobbyFinder.hubby.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.hobbyFinder.hubby.controller.Constants.PageConstants;
import com.hobbyFinder.hubby.controller.routes.NotificationRoutes;
import com.hobbyFinder.hubby.models.dto.notifications.NotificationDto;
import com.hobbyFinder.hubby.services.IServices.NotificationInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hobbyFinder.hubby.models.entities.Notification;
import com.hobbyFinder.hubby.services.ServicesImpl.NotificationService;

@RestController
public class NotificationController {

  private final NotificationInterface notificationService;

  @Autowired
  public NotificationController(NotificationInterface notificationService) {
    this.notificationService = notificationService;
  }

  @GetMapping(NotificationRoutes.GET_BY_USER_AUTH)
  public ResponseEntity<Page<NotificationDto>> getNotifications
          (@RequestParam Optional<Integer> notificationsPerPage,
           @RequestParam Optional<Integer> page) {

    Pageable pageable = PageRequest.of(page.orElse(PageConstants.PAGE_INDEX), notificationsPerPage.orElse(PageConstants.NOTIFICATIONS_PER_PAGE));

    return ResponseEntity
            .status(HttpStatus.OK)
            .body(notificationService.getNotifications(pageable));
  }

}
