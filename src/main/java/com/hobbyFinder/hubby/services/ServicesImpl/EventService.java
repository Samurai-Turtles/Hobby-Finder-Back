package com.hobbyFinder.hubby.services.ServicesImpl;

import com.hobbyFinder.hubby.exception.EntityStateException.EventNotEndedException;
import com.hobbyFinder.hubby.exception.NotFound.EventNotFoundException;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.InadequateUserPosition;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.UserNotInEventException;
import com.hobbyFinder.hubby.models.dto.avaliations.PostAvaliationDto;
import com.hobbyFinder.hubby.models.dto.avaliations.ResponseAvaliationDto;
import com.hobbyFinder.hubby.models.dto.participations.GetResponseParticipationEvent;
import com.hobbyFinder.hubby.models.entities.Avaliation;
import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.models.entities.Participation;
import com.hobbyFinder.hubby.repositories.ParticipationRepository;
import com.hobbyFinder.hubby.services.IServices.UserInterface;
import com.hobbyFinder.hubby.util.GetUserLogged;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.hobbyFinder.hubby.models.dto.events.EventCreateDto;
import com.hobbyFinder.hubby.models.dto.events.EventDto;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.services.IServices.EventInterface;

import java.time.LocalDateTime;
import java.util.Collection;
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
    public List<GetResponseParticipationEvent> getParticipationsEvent(UUID idEvent) {
        Event event = findEvent(idEvent);
        checkUserParticipating(event);
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
        eventRepository.delete(findEvent(uuid));
        eventRepository.flush();
    }

    @Override
    public ResponseAvaliationDto evaluateEvent(UUID idEvent, PostAvaliationDto postAvaliationDTO, LocalDateTime requestTime) {
        Event event = findEvent(idEvent);
        checkUserParticipating(event);
        if (event.getEventEnd().isBefore(requestTime)) {
            throw new EventNotEndedException("evento ainda não foi finalizado");
        }

        Participation participation = getUserParticipationFromEvent(event);
        Avaliation avaliation = new Avaliation(postAvaliationDTO, participation);
        participation.setAvaliation(avaliation);
        this.participationService.saveParticipation(participation);

        updateEventAvaliation(event);
        this.userInterface.updateUserAvaliation(getEventCreatorId(event));
        return new ResponseAvaliationDto(avaliation.getId(), avaliation.getStars(), avaliation.getComment());
    }

    private UUID getEventCreatorId(Event event) {
        return event.getParticipations()
                .stream()
                .filter(p -> p.getPosition().getRank() == 3)
                .findFirst()
                .get()
                .getIdUser();
    }

    private void updateEventAvaliation(Event event) {
        double avgStars = this.participationService.getAvgStarsByEvent(event.getId());
        event.setAvaliationStars(avgStars);
        this.eventRepository.save(event);
    }

    @Override
    public Collection<ResponseAvaliationDto> getAvaliationsEvent(UUID idEvent) {
        Event event = findEvent(idEvent);
        checkUserParticipating(event);

        if (!(getUserParticipationFromEvent(event).getPosition().getRank() == 3)) {
            throw new InadequateUserPosition();
        }

        return this.participationService
                .getAvaliationsFromEvent(event.getId())
                .stream()
                .map(avl -> new ResponseAvaliationDto(avl.getId(), avl.getStars(), avl.getComment()))
                .collect(Collectors.toList());
    }

    private Participation getUserParticipationFromEvent(Event event) {
        return event.getParticipations()
                .stream()
                .filter(p -> p.getIdUser().equals(getUserLogged.getUserLogged().getId()))
                .findFirst()
                .orElseThrow(UserNotInEventException::new);
    }

    private void checkUserParticipating(Event event) {
        if(event.getParticipations()
                .stream()
                .noneMatch(p -> p.getIdUser().equals(getUserLogged.getUserLogged().getId()))) {
            throw new UserNotInEventException();
        }
    }
}
