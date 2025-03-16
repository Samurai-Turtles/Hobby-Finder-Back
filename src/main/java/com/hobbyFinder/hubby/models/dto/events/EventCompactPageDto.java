package com.hobbyFinder.hubby.models.dto.requests.events;

import com.hobbyFinder.hubby.models.enums.PrivacyEnum;

import java.util.Date;
import java.util.UUID;

public record EventCompactPageDto(
        UUID id,
        String Name,
        Date begin,
        Date end,
        PrivacyEnum privacy,
        int maxUserAmmount,
        int userCount
) {
}
