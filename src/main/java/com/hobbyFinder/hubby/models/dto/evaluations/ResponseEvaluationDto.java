package com.hobbyFinder.hubby.models.dto.evaluations;

import com.hobbyFinder.hubby.models.dto.user.UserResponseDTO;

import java.util.UUID;

public record ResponseEvaluationDto(
        UUID id,
        int star,
        String comment,
        UserResponseDTO userDTO
) {
}
