package com.hobbyFinder.hubby.models.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.hobbyFinder.hubby.models.enums.InterestEnum;
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
    private InterestEnum interest;

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

    @OneToMany(cascade = CascadeType.ALL)
    private List<ParticipationRequest> requests;

    private int maxUserAmount;

    @Transient
    private double avaliationStars;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "photo_id")
    private Photo photo = new Photo();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participation> participations;
}
