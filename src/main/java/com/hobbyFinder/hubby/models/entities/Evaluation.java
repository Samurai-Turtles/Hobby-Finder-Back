package com.hobbyFinder.hubby.models.entities;

import com.hobbyFinder.hubby.models.dto.evaluations.PostEvaluationDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Min(value = 0, message = "Avaliação deve ser no mínimo 0")
    @Max(value = 5, message = "Avaliação deve ser no máximo 5")
    private int stars;

    @Column
    private String comment;

    @OneToOne
    @JoinColumn(name = "participation_id", nullable = false)
    private Participation participation;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Evaluation(PostEvaluationDto avaliationDto, Participation participation, User user) {
        this.stars = avaliationDto.stars();
        this.comment = avaliationDto.comment();
        this.participation = participation;
        this.user = user;
    }
}
