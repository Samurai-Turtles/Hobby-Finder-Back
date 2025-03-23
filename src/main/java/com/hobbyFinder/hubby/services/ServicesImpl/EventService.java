package com.hobbyFinder.hubby.services.ServicesImpl;

import com.hobbyFinder.hubby.exception.EventException.InvalidDateException;
import com.hobbyFinder.hubby.exception.NotFound.EventNotFoundException;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.UserNotInEventException;
import com.hobbyFinder.hubby.models.dto.participations.ParticipationCreateDto;
import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.models.entities.Local;
import com.hobbyFinder.hubby.models.entities.Participation;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.models.enums.ParticipationPosition;
import com.hobbyFinder.hubby.models.enums.UserParticipation;
import com.hobbyFinder.hubby.repositories.ParticipationRepository;
import com.hobbyFinder.hubby.repositories.UserRepository;
import com.hobbyFinder.hubby.util.GetUserLogged;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.hobbyFinder.hubby.models.dto.events.EventCreateDto;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.services.IServices.EventInterface;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class EventService implements EventInterface {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private GetUserLogged getUserLogged;

    @Override
    public void registerEvent(EventCreateDto eventCreateDto) {
        checkValidData(eventCreateDto.begin(), eventCreateDto.end());

        Local local = Local.builder().street(eventCreateDto.local().street())
                .district(eventCreateDto.local().district())
                .number(eventCreateDto.local().number())
                .city(eventCreateDto.local().city())
                .state(eventCreateDto.local().state()).build();

        Event event = Event.builder().name(eventCreateDto.Name())
                .EventBegin(eventCreateDto.begin())
                .EventEnd(eventCreateDto.end())
                .local(local)
                .privacy(eventCreateDto.privacy())
                .description(eventCreateDto.description())
                .maxUserAmount(eventCreateDto.maxUserAmount())
                .participations(new ArrayList<Participation>())
                .build();

        this.eventRepository.save(event);

        Participation participation = newParticipation(
                new ParticipationCreateDto(event.getId(), getUserLogged.getUserLogged().getId(),
                        UserParticipation.CONFIRMED_PRESENCE, ParticipationPosition.CREATOR));
        event.getParticipations().add(participation);

        User user = getUserLogged.getUserLogged();
        user.getParticipations().add(participation);
        this.userRepository.save(user);
        this.eventRepository.save(event);
    }

    private void checkValidData(LocalDateTime begin, LocalDateTime end) {
        if (begin.isAfter(end)) {
            throw new InvalidDateException();
        }
    }

    private Participation newParticipation(ParticipationCreateDto participationDTO) {
        Participation participation = Participation.builder().idEvent(participationDTO.idEvent())
                .idUser(participationDTO.idUser())
                .userParticipation(participationDTO.userParticipation())
                .position(participationDTO.participationPosition())
                .avaliation(null)
                .build();
        participationRepository.save(participation);
        return participation;
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
        if (event.getParticipations()
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
