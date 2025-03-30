package com.hobbyFinder.hubby.models.dto.participationRequest;

import java.util.UUID;

public record UserRequestResponse(
        UUID id,
        String usuario,
        String bio) {
}
