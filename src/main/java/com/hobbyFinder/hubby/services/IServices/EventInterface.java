package com.hobbyFinder.hubby.services.IServices;

import com.hobbyFinder.hubby.models.dto.events.EventCreateDto;
import com.hobbyFinder.hubby.models.dto.events.EventDto;
import com.hobbyFinder.hubby.models.dto.events.EventPutDto;
import com.hobbyFinder.hubby.models.entities.Event;
import java.util.UUID;

public interface EventInterface {

    void registerEvent(EventCreateDto eventCreateDto);
    Event findEvent(UUID idEvent);
    void checkPermission(UUID idEvent);
    void deleteEvent(UUID uuid);
    void updateEventAvaliation(UUID idEvent, double stars);
    void checkUserParticipating(Event event);
    UUID getEventOwnerId(Event event);

    EventDto updateEvent(UUID id, EventPutDto eventPutDto);
    EventDto getEvent(UUID id);
}
