package com.hobbyFinder.hubby.models.dto.notifications;

import com.hobbyFinder.hubby.models.dto.photos.PhotoDto;
import com.hobbyFinder.hubby.models.dto.user.UserResponseDTO;
import com.hobbyFinder.hubby.models.enums.NotificationEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

public record NotificationDto(
        UUID id,
        String message,
        PhotoDto photo,
        UserResponseDTO userDto,
        LocalDateTime date,
        UUID idEVento,
        UUID idSolicitacao,
        NotificationEnum notificationEnum) {
}
