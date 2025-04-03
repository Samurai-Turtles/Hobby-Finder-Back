package com.hobbyFinder.hubby.models.dto.situations;

import com.hobbyFinder.hubby.models.enums.SituationEnum;

import java.util.UUID;


public record SituationDto(
        SituationEnum situation,
        UUID idParticipation,
        UUID idParticipationRequest) {}
