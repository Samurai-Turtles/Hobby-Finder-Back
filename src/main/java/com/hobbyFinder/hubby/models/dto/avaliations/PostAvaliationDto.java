package com.hobbyFinder.hubby.models.dto.avaliations;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostAvaliationDto(
        @NotNull @Size(max = 5, message = "Avaliação entre 0 a 5 estrelas") int stars,
        String comment
) {
}
