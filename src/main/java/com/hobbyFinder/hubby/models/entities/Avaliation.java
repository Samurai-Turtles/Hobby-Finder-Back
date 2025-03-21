package com.hobbyFinder.hubby.models.entities;

import com.hobbyFinder.hubby.models.dto.avaliations.PostAvaliationDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Avaliation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private UUID id;

    @Column(nullable = false)
    @Size(min = 0, max = 5)
    @Getter
    private int stars;

    @Column()
    @Getter
    private String comment;

    @ManyToOne
    @JoinColumn(name = "participation_id", nullable = false)
    private Participation participation;

    public Avaliation(PostAvaliationDto avaliationDto, Participation participation) {
        this.stars = avaliationDto.stars();
        this.comment = avaliationDto.comment();
        this.participation = participation;
    }
}
