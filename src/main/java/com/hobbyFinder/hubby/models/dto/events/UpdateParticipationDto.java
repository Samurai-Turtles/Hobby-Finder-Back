package com.hobbyFinder.hubby.models.dto.events;

import com.hobbyFinder.hubby.models.entities.UserParticipation;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateParticipationDto(
        @NotNull UUID idEvent,
        @NotNull UUID idParticipation,
        @NotNull UserParticipation participation) {}