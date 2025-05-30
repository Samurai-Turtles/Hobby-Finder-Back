package com.hobbyFinder.hubby.services.ServicesImpl;

import com.hobbyFinder.hubby.exception.EventException.EventNotEndedException;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.InadequateUserPosition;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.UserNotInEventException;
import com.hobbyFinder.hubby.models.dto.evaluations.PostEvaluationDto;
import com.hobbyFinder.hubby.models.dto.evaluations.ResponseEvaluationDto;
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
import java.util.List;
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
    public ResponseEvaluationDto evaluateEvent(UUID idEvent, PostEvaluationDto postAvaliationDTO, LocalDateTime requestTime) {
        Event event = this.eventInterface.findEvent(idEvent);
        this.eventInterface.checkUserParticipating(event);

        if (requestTime.isBefore(event.getEventEnd())) {
            throw new EventNotEndedException();
        }

        Participation participation = getParticipationFromEvent(event);
        Evaluation avaliation = new Evaluation(postAvaliationDTO, participation);
        participation.setEvaluation(avaliation);
        this.participationRepository.save(participation);

        this.eventInterface.updateEventAvaliation(event.getId(), getAvgStarsByEvent(event.getId()));

        UUID idEventOwner = event.getCreator().getId();
        this.userInterface.updateUserAvaliation(idEventOwner, getAvgStarsByUser(idEventOwner));
        return new ResponseEvaluationDto(avaliation.getId(), avaliation.getStars(), avaliation.getComment());
    }

    @Override
    public Collection<ResponseEvaluationDto> getEventAvaliations(UUID idEvent) {
        Event event = this.eventInterface.findEvent(idEvent);
        this.eventInterface.checkUserParticipating(event);

        if (!(getParticipationFromEvent(event).getPosition().getRank() == 2)) {
            throw new InadequateUserPosition();
        }

        return this.participationRepository.getAvaliationByEventOrdered(event.getId())
                .stream()
                .map(avl -> new ResponseEvaluationDto(avl.getId(), avl.getStars(), avl.getComment()))
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
