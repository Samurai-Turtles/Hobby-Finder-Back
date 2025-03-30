package com.hobbyFinder.hubby.models.dto.participations;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ParticipationDto(
        @NotNull UUID idEvent,
        @NotNull UUID idParticipation) {}
