package com.hobbyFinder.hubby.models.dto.participationRequest;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record ParticipationRequestUserDto(
        @NotNull UUID id,
        @NotNull UserRequestResponse usuario) {
}
