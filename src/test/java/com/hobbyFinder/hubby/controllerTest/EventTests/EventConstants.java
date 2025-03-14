package com.hobbyFinder.hubby.controllerTest.EventTests;

import com.hobbyFinder.hubby.models.dto.events.EventDto;
import com.hobbyFinder.hubby.models.dto.LocalDto;
import com.hobbyFinder.hubby.models.enums.PrivacyEnum;

import java.time.LocalDateTime;
import java.util.UUID;

public class EventConstants {
    private static final LocalDto DEFAULT_LOCAL = new LocalDto(
            -23.55052, // Latitude
            -46.633308, // Longitude
            "123 Street",
            "Central District",
            "42",
            "São Paulo",
            "SP"
    );

    public static final EventDto EVENT_DTO_CREATE = new EventDto(
            UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            "Event Created",
            LocalDateTime.of(2025, 1, 1, 12, 0),
            LocalDateTime.of(2025, 1, 1, 14, 0),
            DEFAULT_LOCAL,
            PrivacyEnum.PUBLIC,
            "Description of created event",
            100,
            10
    );

    public static final EventDto EVENT_DTO_UPDATE = new EventDto(
            UUID.fromString("123e4567-e89b-12d3-a456-426614174001"),
            "Event Updated",
            LocalDateTime.of(2025, 2, 1, 12, 0),
            LocalDateTime.of(2025, 2, 1, 14, 0),
            DEFAULT_LOCAL,
            PrivacyEnum.PRIVATE,
            "Description of updated event",
            50,
            5
    );

    public static final EventDto EVENT_DTO = new EventDto(
            UUID.fromString("123e4567-e89b-12d3-a456-426614174002"),
            "Regular Event",
            LocalDateTime.of(2025, 3, 1, 12, 0),
            LocalDateTime.of(2025, 3, 1, 14, 0),
            DEFAULT_LOCAL,
            PrivacyEnum.PRIVATE,
            "Description of regular event",
            200,
            20
    );

    public static final EventDto EVENT_DTO_INVALID = new EventDto(
            null, // ID inválido
            "", // Nome inválido
            null, // Data de início inválida
            null, // Data de término inválida
            null, // Local inválido
            null, // Privacidade inválida
            "", // Descrição vazia
            -1, // Número máximo de usuários inválido
            -5 // Contagem de usuários inválida
    );
}
