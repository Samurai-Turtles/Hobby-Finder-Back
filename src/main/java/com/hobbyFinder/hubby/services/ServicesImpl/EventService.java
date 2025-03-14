package com.hobbyFinder.hubby.services.ServicesImpl;

import com.hobbyFinder.hubby.exception.NotFound.EventNotFoundException;
import com.hobbyFinder.hubby.models.entities.Event;
import org.springframework.stereotype.Service;

import com.hobbyFinder.hubby.models.dto.events.EventCreateDto;
import com.hobbyFinder.hubby.models.dto.events.EventDto;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.services.IServices.EventInterface;

import java.util.UUID;

@Service
public class EventService implements EventInterface{

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public EventDto registerEvent(EventCreateDto eventCreateDto) {
        return null;
    }

    @Override
    public void deleteEvent(UUID uuid) throws EventNotFoundException {
        Event evento = eventRepository.findById(uuid)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado."));
        eventRepository.delete(evento);
        eventRepository.flush();
    }
}
