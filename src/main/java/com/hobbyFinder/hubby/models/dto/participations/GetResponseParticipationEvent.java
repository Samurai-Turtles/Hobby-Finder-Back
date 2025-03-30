package com.hobbyFinder.hubby.models.dto.participations;

import com.hobbyFinder.hubby.models.enums.UserParticipation;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GetResponseParticipationEvent(
        @NotNull UUID userId,
        @NotNull UserParticipation userParticipation) { }
