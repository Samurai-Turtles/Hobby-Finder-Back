package com.hobbyFinder.hubby.models.entities;

import com.hobbyFinder.hubby.models.dto.avaliations.PostAvaliationDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Avaliation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Size(min = 0, max = 5)
    private int stars;

    @Column
    private String comment;

    @OneToOne
    @JoinColumn(name = "participation_id", nullable = false)
    private Participation participation;

    public Avaliation(PostAvaliationDto avaliationDto, Participation participation) {
        this.stars = avaliationDto.stars();
        this.comment = avaliationDto.comment();
        this.participation = participation;
    }
}
