package com.hobbyFinder.hubby.models.dto.participations;

import com.hobbyFinder.hubby.models.enums.UserParticipation;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GetResponseParticipationsUser(
        @NotNull UUID idEvento,
        @NotNull UserParticipation userParticipation) {
}
