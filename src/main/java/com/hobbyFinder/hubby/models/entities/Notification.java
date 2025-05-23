package com.hobbyFinder.hubby.models.entities;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "notifications")
@Entity(name = "Notification")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(nullable = false)
  private String message;

  @OneToOne
  @JoinColumn(name = "photo_id")
  private Photo photo;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  private LocalDate date = LocalDate.now();

  public Notification(User user, String message, Photo photo) {
    this.user = user;
    this.message = message;
    this.photo = photo;
  }

}
