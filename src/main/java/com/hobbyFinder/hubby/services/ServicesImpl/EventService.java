package com.hobbyFinder.hubby.services.ServicesImpl;

import com.hobbyFinder.hubby.exception.NotFound.EventNotFoundException;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.UserNotInEventException;
import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.services.IServices.UserInterface;
import com.hobbyFinder.hubby.util.GetUserLogged;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.hobbyFinder.hubby.models.dto.events.EventCreateDto;
import com.hobbyFinder.hubby.models.dto.events.EventDto;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.services.IServices.EventInterface;

import java.util.UUID;

@Service
public class EventService implements EventInterface{

    @Autowired
    private final EventRepository eventRepository;

    @Autowired
    @Lazy
    private GetUserLogged getUserLogged;

    @Autowired
    @Lazy
    private ParticipationServiceImpl participationService;

    @Autowired
    private UserInterface userInterface;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public EventDto registerEvent(EventCreateDto eventCreateDto) {
        return null;
    }

    @Override
    public Event findEvent(UUID idEvent) {
        return eventRepository.findById(idEvent)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado."));
    }

    public void deleteEvent(UUID uuid) throws EventNotFoundException {
        eventRepository.delete(findEvent(uuid));
        eventRepository.flush();
    }

    @Override
    public void updateEventAvaliation(UUID idEvent, double stars) {
        Event event = findEvent(idEvent);
        event.setAvaliationStars(stars);
        this.eventRepository.save(event);
    }

    @Override
    public void checkUserParticipating(Event event) {
        if(event.getParticipations()
                .stream()
                .noneMatch(p -> p.getIdUser().equals(getUserLogged.getUserLogged().getId()))) {
            throw new UserNotInEventException();
        }
    }

    @Override
    public UUID getEventOwnerId(Event event) {
        return event.getParticipations()
                .stream()
                .filter(p -> p.getPosition().getRank() == 3)
                .findFirst()
                .get()
                .getIdUser();
    }
}
