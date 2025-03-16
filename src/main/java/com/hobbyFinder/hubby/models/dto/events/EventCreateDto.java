package com.hobbyFinder.hubby.models.dto.requests.events;

import java.time.LocalDateTime;

import com.hobbyFinder.hubby.models.dto.LocalDto;
import com.hobbyFinder.hubby.models.enums.PrivacyEnum;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EventCreateDto(
                @NotBlank String Name,

                @NotNull LocalDateTime begin,

                @NotNull LocalDateTime end,

                @NotNull @Valid LocalDto local,

                @NotNull PrivacyEnum privacy,

                @NotNull String description,

                int MaxUserAmmount) {
}
