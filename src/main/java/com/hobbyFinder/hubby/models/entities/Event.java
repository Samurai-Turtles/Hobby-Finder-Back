package com.hobbyFinder.hubby.models.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.hobbyFinder.hubby.models.enums.PrivacyEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Builder
@Data
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime EventBegin;

    @Column(nullable = false)
    private LocalDateTime EventEnd;

    @Column(nullable = false)
    @Embedded
    private Local local;

    @Column(nullable = false)
    private PrivacyEnum privacy;

    @Column(nullable = false)
    private String description;

    private int maxUserAmout;
}
