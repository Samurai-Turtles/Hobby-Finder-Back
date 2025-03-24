package com.hobbyFinder.hubby.services.ServicesImpl;

import com.hobbyFinder.hubby.exception.EventException.EventNotEndedException;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.InadequateUserPosition;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.UserNotInEventException;
import com.hobbyFinder.hubby.models.dto.avaliations.PostAvaliationDto;
import com.hobbyFinder.hubby.models.dto.avaliations.ResponseAvaliationDto;
import com.hobbyFinder.hubby.models.entities.Evaluation;
import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.models.entities.Participation;
import com.hobbyFinder.hubby.repositories.ParticipationRepository;
import com.hobbyFinder.hubby.services.IServices.EvaluationInterface;
import com.hobbyFinder.hubby.services.IServices.EventInterface;
import com.hobbyFinder.hubby.services.IServices.UserInterface;
import com.hobbyFinder.hubby.util.GetUserLogged;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EvaluationService implements EvaluationInterface {

    private EventInterface eventInterface;
    private UserInterface userInterface;

    private GetUserLogged getUserLogged;
    private ParticipationRepository participationRepository;

    @Override
    public ResponseAvaliationDto evaluateEvent(UUID idEvent, PostAvaliationDto postAvaliationDTO, LocalDateTime requestTime) {
        Event event = this.eventInterface.findEvent(idEvent);
        this.eventInterface.checkUserParticipating(event);

        if (event.getEventEnd().isBefore(requestTime)) {
            throw new EventNotEndedException();
        }

        Participation participation = getParticipationFromEvent(event);
        Evaluation avaliation = new Evaluation(postAvaliationDTO, participation);
        participation.setEvaluation(avaliation);
        this.participationRepository.save(participation);

        this.eventInterface.updateEventAvaliation(event.getId(), getAvgStarsByEvent(event.getId()));

        UUID idEventOwner = this.eventInterface.getEventOwnerId(event);
        this.userInterface.updateUserAvaliation(idEventOwner, getAvgStarsByUser(idEventOwner));
        return new ResponseAvaliationDto(avaliation.getId(), avaliation.getStars(), avaliation.getComment());
    }

    @Override
    public Collection<ResponseAvaliationDto> getEventAvaliations(UUID idEvent) {
        Event event = this.eventInterface.findEvent(idEvent);
        this.eventInterface.checkUserParticipating(event);

        if (!(getParticipationFromEvent(event).getPosition().getRank() == 3)) {
            throw new InadequateUserPosition();
        }

        return this.participationRepository.getAvaliationByEventOrdered(event.getId())
                .stream()
                .map(avl -> new ResponseAvaliationDto(avl.getId(), avl.getStars(), avl.getComment()))
                .collect(Collectors.toList());
    }

    private Participation getParticipationFromEvent(Event event) {
        return event.getParticipations()
                .stream()
                .filter(p -> p.getIdUser().equals(getUserLogged.getUserLogged().getId()))
                .findFirst()
                .orElseThrow(UserNotInEventException::new);
    }

    private double getAvgStarsByEvent(UUID idEvent) {
        return this.participationRepository.avgStarsByEvent(idEvent);
    }

    private double getAvgStarsByUser(UUID idUser) {
        return this.participationRepository.findAverageStarsByUser(idUser);
    }

}
