package com.hobbyFinder.hubby.models.dto.requests.events;

import java.util.List;

public record EventPageDto(int page, int eventPage, List<EventCompactPageDto> events) {
}
