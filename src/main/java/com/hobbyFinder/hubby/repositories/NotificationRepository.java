package com.hobbyFinder.hubby.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hobbyFinder.hubby.models.entities.Notification;

public interface NotificationRepository
  extends JpaRepository<Notification, UUID> {
  List<Notification> findByUser_Id(UUID userId, Pageable pageable);
}
