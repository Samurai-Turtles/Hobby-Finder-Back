package com.hobbyFinder.hubby.services.IServices;

import com.hobbyFinder.hubby.models.dto.events.EventCreateDto;
import com.hobbyFinder.hubby.models.entities.Event;
import java.util.UUID;

public interface EventInterface {

    void registerEvent(EventCreateDto eventCreateDto);
    Event findEvent(UUID idEvent);
    void deleteEvent(UUID uuid);
    void updateEventAvaliation(UUID idEvent, double stars);
    void checkUserParticipating(Event event);
    UUID getEventOwnerId(Event event);
}
