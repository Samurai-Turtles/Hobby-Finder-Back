package com.hobbyFinder.hubby.models.dto.user;

import com.hobbyFinder.hubby.models.enums.InterestEnum;

import java.util.List;
import java.util.UUID;

public record UserResponseDTO(UUID id, String username, String fullName, String bio, List<InterestEnum> interests) { }
