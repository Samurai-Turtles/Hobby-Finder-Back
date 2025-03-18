package com.hobbyFinder.hubby.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "participations")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idParticipation;

    @JoinColumn(name = "event_id", nullable = false)
    private UUID idEvent;

    @JoinColumn(name = "user_id", nullable = false)
    private UUID idUser;

}
