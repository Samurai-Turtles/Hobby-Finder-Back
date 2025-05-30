package com.hobbyFinder.hubby.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hobbyFinder.hubby.models.entities.Notification;

public interface NotificationRepository
  extends JpaRepository<Notification, UUID> {
  @Query("SELECT n FROM Notification n WHERE n.user.id = :userId")
  Page<Notification> findByUserId(
    @Param("userId") UUID userId,
    Pageable pageable
  );
}
