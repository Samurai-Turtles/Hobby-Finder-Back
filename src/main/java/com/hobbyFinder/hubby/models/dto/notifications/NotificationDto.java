package com.hobbyFinder.hubby.models.dto.notifications;

import com.hobbyFinder.hubby.models.dto.photos.PhotoDto;
import com.hobbyFinder.hubby.models.dto.user.UserDTO;
import com.hobbyFinder.hubby.models.dto.user.UserResponseDTO;

import java.time.LocalDate;
import java.util.UUID;

public record NotificationDto(
        UUID id,
        String message,
        PhotoDto photo,
        UserResponseDTO userDto,
        LocalDate date) {
}
