package com.hobbyFinder.hubby.models.entities;

import com.hobbyFinder.hubby.models.enums.PrivacyEnum;

import java.util.Date;
import java.util.UUID;

public record Event(
        UUID id,
        String Name,
        Date begin,
        Date end,
        Local local,
        PrivacyEnum privacy,
        String description,
        int MaxUserAmmount) {
}
