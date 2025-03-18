package com.hobbyFinder.hubby.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Table(name = "participations")
@Getter
@Setter
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

    private UserParticipation userParticipation;

    public Participation(UUID idParticipation, UUID idEvent, UUID idUser) {
        this.idParticipation = idParticipation;
        this.idEvent = idEvent;
        this.idUser = idUser;
        this.userParticipation = UserParticipation.UNCONFIRMED_PRESENCE;
    }

}
