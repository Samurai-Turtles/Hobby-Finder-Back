package com.hobbyFinder.hubby.models.dto.events;

import com.hobbyFinder.hubby.models.entities.UserParticipation;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GetParticipationEvent(@NotNull UUID userId, @NotNull UserParticipation userParticipation) { }
