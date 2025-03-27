package com.hobbyFinder.hubby.models.dto.evaluations;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PostEvaluationDto(
        @Min(value = 0, message = "Avaliação deve ser no mínimo 0")
        @Max(value = 5, message = "Avaliação deve ser no máximo 5")
        int stars,
        String comment
) {
}
