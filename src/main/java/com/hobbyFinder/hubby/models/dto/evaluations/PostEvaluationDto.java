package com.hobbyFinder.hubby.models.dto.evaluations;

import jakarta.validation.constraints.Size;

public record PostEvaluationDto(
        @Size(max = 5, message = "Avaliação entre 0 a 5 estrelas") int stars,
        String comment
) {
}
