package com.hobbyFinder.hubby.models.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.hobbyFinder.hubby.models.enums.PrivacyEnum;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@Getter
@Setter
@RequiredArgsConstructor
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

    @Column(name = "max_user_amount", nullable = false)
    private int maxUserAmount;

    @Transient
    private double avaliationStars;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participation> participations;

    public Event(String name, LocalDateTime eventBegin, LocalDateTime eventEnd, Local local, PrivacyEnum privacy, String description, int maxUserAmount) {
        this.name = name;
        this.EventBegin = eventBegin;
        this.EventEnd = eventEnd;
        this.local = local;
        this.privacy = privacy;
        this.description = description;
        this.maxUserAmount = maxUserAmount;
        this.participations = new ArrayList<>();
    }
}
