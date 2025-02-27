package com.hobbyFinder.hubby.models.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.hobbyFinder.hubby.models.enums.PrivacyEnum;

public record Event(
                UUID id,
                String Name,
                LocalDateTime begin,
                LocalDateTime end,
                Local local,
                PrivacyEnum privacy,
                String description,
                int MaxUserAmmount) {
}
