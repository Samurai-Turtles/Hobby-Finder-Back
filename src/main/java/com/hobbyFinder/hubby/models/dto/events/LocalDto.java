package com.hobbyFinder.hubby.models.dto.events;

import jakarta.validation.constraints.NotNull;

public record LocalDto(
        @NotNull String street,
        @NotNull String district,
        @NotNull String number,
        @NotNull String city,
        @NotNull String state,
        double longitude,
        double latitude) {
}
