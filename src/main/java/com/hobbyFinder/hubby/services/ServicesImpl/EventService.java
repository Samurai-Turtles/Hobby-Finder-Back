package com.hobbyFinder.hubby.services.ServicesImpl;

import com.hobbyFinder.hubby.exception.NotFound.EventNotFoundException;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.UserNotInEventException;
import com.hobbyFinder.hubby.models.dto.participations.GetResponseParticipationEvent;
import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.util.GetUserLogged;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    @Autowired
    @Lazy
    private GetUserLogged getUserLogged;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public EventDto registerEvent(EventCreateDto eventCreateDto) {
        return null;
    }

    @Override
    public List<GetResponseParticipationEvent> getParticipationsEvent(UUID idEvent) {
        Event event = findEvent(idEvent);
        if(event.getParticipations().stream().anyMatch(p -> p.getIdUser().equals(getUserLogged.getUserLogged().getId()))) {
            throw new UserNotInEventException();
        }
        return event.getParticipations().stream()
                .map(participacao -> new GetResponseParticipationEvent(participacao.getIdUser(), participacao.getUserParticipation()))
                .collect(Collectors.toList());
    }

    @Override
    public Event findEvent(UUID idEvent) {
        return eventRepository.findById(idEvent)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado."));
    }
    public void deleteEvent(UUID uuid) throws EventNotFoundException {
        Event evento = eventRepository.findById(uuid)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado."));
        eventRepository.delete(evento);
        eventRepository.flush();
    }
}
