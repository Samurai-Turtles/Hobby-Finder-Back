package com.hobbyFinder.hubby.models.dto.user;

import com.hobbyFinder.hubby.models.enums.UserRole;

public record UserDTO (String email, String username, UserRole role) {}
