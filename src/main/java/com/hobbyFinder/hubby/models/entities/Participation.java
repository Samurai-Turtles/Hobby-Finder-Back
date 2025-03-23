package com.hobbyFinder.hubby.models.entities;

import com.hobbyFinder.hubby.models.enums.ParticipationPosition;
import com.hobbyFinder.hubby.models.enums.UserParticipation;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "participations")
@Getter
@Setter
@Builder
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

    @Column(nullable = false)
    private UserParticipation userParticipation;

    @Column(nullable = false)
    private ParticipationPosition position;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "avaliation_id")
    private Avaliation avaliation;

    public boolean isOrganizerParticipation() {
        return ParticipationPosition.ADMIN.getRank() <= this.position.getRank();
    }
}
