package com.hobbyFinder.hubby.services.ServicesImpl;

import com.hobbyFinder.hubby.exception.NotFound.EventNotFoundException;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.UserNotInEventException;
import com.hobbyFinder.hubby.models.dto.events.LocalDto;
import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.models.entities.Local;
import com.hobbyFinder.hubby.util.GetUserLogged;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.hobbyFinder.hubby.models.dto.events.EventCreateDto;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.services.IServices.EventInterface;

import java.util.UUID;

@Service
public class EventService implements EventInterface{

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    @Lazy
    private GetUserLogged getUserLogged;

    @Override
    public void registerEvent(EventCreateDto eventCreateDto) {
        Local local = new Local(eventCreateDto.local().street(), eventCreateDto.local().district(), eventCreateDto.local().number(),
                eventCreateDto.local().city(), eventCreateDto.local().state());
        Event event = new Event(eventCreateDto.Name(), eventCreateDto.begin(), eventCreateDto.end(),
                local, eventCreateDto.privacy(), eventCreateDto.description(), eventCreateDto.maxUserAmount());
        this.eventRepository.save(event);
    }

    @Override
    public Event findEvent(UUID idEvent) {
        return eventRepository.findById(idEvent)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado."));
    }

    public void deleteEvent(UUID uuid) {
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
