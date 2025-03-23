package com.hobbyFinder.hubby.models.dto.photos;

import java.util.UUID;

public record PhotoDto (UUID id, String extension, boolean isSaved) {}
