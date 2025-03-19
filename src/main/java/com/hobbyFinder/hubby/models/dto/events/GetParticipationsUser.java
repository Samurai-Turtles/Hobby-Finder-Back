package com.hobbyFinder.hubby.models.dto.events;

import com.hobbyFinder.hubby.models.enums.UserParticipation;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GetParticipationsUser(
        @NotNull UUID idEvento,
        @NotNull UserParticipation userParticipation) {
}
