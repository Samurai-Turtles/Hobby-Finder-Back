package com.hobbyFinder.hubby.models.dto.user;

import com.hobbyFinder.hubby.models.entities.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}