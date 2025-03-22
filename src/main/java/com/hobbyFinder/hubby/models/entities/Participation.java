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

    @JoinColumn(name = "event_id", nullable = false)
    private UUID idEvent;

    @JoinColumn(name = "user_id", nullable = false)
    private UUID idUser;

    private UserParticipation userParticipation;
    private ParticipationPosition position;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "avaliation_id")
    private Avaliation avaliation;

    public Participation(UUID idEvent, UUID idUser) {
        this.idEvent = idEvent;
        this.idUser = idUser;
        this.userParticipation = UserParticipation.UNCONFIRMED_PRESENCE;
        this.position = ParticipationPosition.PARTICIPANT;
        this.avaliation = new Avaliation();
    }

}
