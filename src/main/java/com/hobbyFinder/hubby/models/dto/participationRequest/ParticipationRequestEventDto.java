package com.hobbyFinder.hubby.models.dto.participationRequest;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record ParticipationRequestEventDto(
        @NotNull UUID id,
        @NotNull UserRequestResponse usuario) {
}
