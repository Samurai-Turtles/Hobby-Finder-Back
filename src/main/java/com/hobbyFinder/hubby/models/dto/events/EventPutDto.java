package com.hobbyFinder.hubby.models.dto.events;

import java.time.LocalDateTime;

import com.hobbyFinder.hubby.models.enums.InterestEnum;
import com.hobbyFinder.hubby.models.enums.PrivacyEnum;

public record EventPutDto(
        String Name,
        LocalDateTime begin,
        LocalDateTime end,
        LocalDto local,
        PrivacyEnum privacy,
        String description,
        Integer maxUserAmount,
        InterestEnum interestEnum) {
}
