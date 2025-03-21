package com.hobbyFinder.hubby.models.dto.avaliations;

import java.util.UUID;

public record ResponseAvaliationDto(
        UUID id,
        int star,
        String comment
) {
}
