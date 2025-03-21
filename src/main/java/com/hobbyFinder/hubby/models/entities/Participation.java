package com.hobbyFinder.hubby.models.entities;

import com.hobbyFinder.hubby.models.enums.ParticipationPosition;
import com.hobbyFinder.hubby.models.enums.UserParticipation;
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

    @JoinColumn(name = "event_id", referencedColumnName = "id", nullable = false)
    private UUID idEvent;

    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UUID idUser;

    private UserParticipation userParticipation;
    private ParticipationPosition position;

    public Participation(UUID idParticipation, UUID idEvent, UUID idUser) {
        this.idParticipation = idParticipation;
        this.idEvent = idEvent;
        this.idUser = idUser;
        this.userParticipation = UserParticipation.UNCONFIRMED_PRESENCE;
        this.position = ParticipationPosition.PARTICIPANT;
    }

}
