package com.hobbyFinder.hubby.models.dto.events;

import com.hobbyFinder.hubby.models.dto.LocalDto;
import com.hobbyFinder.hubby.models.entities.Local;
import com.hobbyFinder.hubby.models.enums.PrivacyEnum;

import java.util.Date;
import java.util.UUID;

public record EventCreateDto(
        String Name,
        Date begin,
        Date end,
        LocalDto local,
        PrivacyEnum privacy,
        String description,
        int MaxUserAmmount) {
}
