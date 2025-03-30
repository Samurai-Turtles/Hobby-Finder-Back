package com.hobbyFinder.hubby.models.dto.participations;

import com.hobbyFinder.hubby.models.entities.Participation;
import com.hobbyFinder.hubby.models.enums.ParticipationPosition;
import com.hobbyFinder.hubby.models.enums.UserParticipation;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateParticipationDto(
        @NotNull UserParticipation participation,
        @NotNull ParticipationPosition position) {}