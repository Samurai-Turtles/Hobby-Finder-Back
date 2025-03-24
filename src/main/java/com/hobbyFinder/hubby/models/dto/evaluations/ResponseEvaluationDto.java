package com.hobbyFinder.hubby.models.dto.evaluations;

import java.util.UUID;

public record ResponseEvaluationDto(
        UUID id,
        int star,
        String comment
) {
}
