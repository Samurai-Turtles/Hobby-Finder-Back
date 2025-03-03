package com.hobbyFinder.hubby.models.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.hobbyFinder.hubby.models.enums.PrivacyEnum;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Column;
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
    private String Name;

    @Column(nullable = false)
    private LocalDateTime begin;

    @Column(nullable = false)
    private LocalDateTime end;

    @Column(nullable = false)
    private Local local;

    @Column(nullable = false)
    private PrivacyEnum privacy;

    @Column(nullable = false)
    private String description;

    private int MaxUserAmmoun;
}