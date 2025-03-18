package com.hobbyFinder.hubby.services.ServicesImpl;

import com.hobbyFinder.hubby.exception.NotFound.EventNotFoundException;
import com.hobbyFinder.hubby.models.dto.events.GetParticipationEvent;
import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.models.entities.Participation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hobbyFinder.hubby.models.dto.events.EventCreateDto;
import com.hobbyFinder.hubby.models.dto.events.EventDto;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.services.IServices.EventInterface;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventService implements EventInterface{

    @Autowired
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public EventDto registerEvent(EventCreateDto eventCreateDto) {
        return null;
    }

    @Override
    public List<GetParticipationEvent> getParticipationsEvent(UUID idEvent) {
        Event event = findEvent(idEvent);
        return event.getParticipations().stream()
                .map(participacao -> new GetParticipationEvent(participacao.getIdUser(), participacao.getUserParticipation()))
                .collect(Collectors.toList());
    }

    @Override
    public Event findEvent(UUID idEvent) {
        return eventRepository.findById(idEvent)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado."));
    }
}
