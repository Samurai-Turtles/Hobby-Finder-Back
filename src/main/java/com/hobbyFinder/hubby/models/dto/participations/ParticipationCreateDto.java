package com.hobbyFinder.hubby.models.dto.participations;

import com.hobbyFinder.hubby.models.enums.ParticipationPosition;
import com.hobbyFinder.hubby.models.enums.UserParticipation;

import java.util.UUID;

public record ParticipationCreateDto(
        UUID idEvent,
        UUID idUser,
        UserParticipation userParticipation,
        ParticipationPosition participationPosition

) {
}
