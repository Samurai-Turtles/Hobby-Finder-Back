package com.hobbyFinder.hubby.models.dto.requests.events;

import java.time.LocalDateTime;
import java.util.UUID;

import com.hobbyFinder.hubby.models.dto.LocalDto;
import com.hobbyFinder.hubby.models.enums.PrivacyEnum;

public record EventDto(
        UUID id,
        String Name,
        LocalDateTime begin,
        LocalDateTime end,
        LocalDto local,
        PrivacyEnum privacy,
        String description,
        int MaxUserAmmount,
        int userCount) {
}