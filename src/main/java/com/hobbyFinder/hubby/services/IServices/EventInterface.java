package com.hobbyFinder.hubby.services.IServices;

import com.hobbyFinder.hubby.models.dto.events.EventCreateDto;
import com.hobbyFinder.hubby.models.dto.events.EventDto;
import com.hobbyFinder.hubby.models.dto.events.EventPutDto;
import com.hobbyFinder.hubby.models.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface EventInterface {

    EventDto registerEvent(EventCreateDto eventCreateDto);
    Event findEvent(UUID idEvent);
    void checkPermission(UUID idEvent);
    void deleteEvent(UUID uuid);
    void updateEventAvaliation(UUID idEvent, double stars);
    boolean checkUserParticipating(Event event);
    EventDto updateEvent(UUID id, EventPutDto eventPutDto);
    EventDto getEvent(UUID id);

    Page<EventDto> getEventByAuthUser(Optional<Double> latitude, Optional<Double> longitude, Optional<String> name, Pageable pageable);

    Page<EventDto> getByUserId(UUID userId, Optional<String> name, Pageable pageable);
}
