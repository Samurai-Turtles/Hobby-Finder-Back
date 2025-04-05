package com.hobbyFinder.hubby.services.ServicesImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hobbyFinder.hubby.exception.EventException.EventNotEndedException;
import com.hobbyFinder.hubby.exception.NotFound.ParticipationNotFoundException;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.InadequateUserPosition;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.UserIdConflictException;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.UserNotInEventException;
import com.hobbyFinder.hubby.models.dto.evaluations.PostEvaluationDto;
import com.hobbyFinder.hubby.models.dto.evaluations.ResponseEvaluationDto;
import com.hobbyFinder.hubby.models.dto.photos.PhotoDto;
import com.hobbyFinder.hubby.models.dto.situations.UserRateSituationDto;
import com.hobbyFinder.hubby.models.dto.user.UserResponseDTO;
import com.hobbyFinder.hubby.models.entities.Evaluation;
import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.models.entities.Participation;
import com.hobbyFinder.hubby.models.entities.Photo;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.models.enums.RateSituationEnum;
import com.hobbyFinder.hubby.repositories.ParticipationRepository;
import com.hobbyFinder.hubby.services.IServices.EvaluationInterface;
import com.hobbyFinder.hubby.services.IServices.EventInterface;
import com.hobbyFinder.hubby.services.IServices.UserInterface;
import com.hobbyFinder.hubby.util.GetUserLogged;

import lombok.AllArgsConstructor;

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
        Evaluation avaliation = new Evaluation(postAvaliationDTO, participation, getUserLogged.getUserLogged());
        participation.setEvaluation(avaliation);
        this.participationRepository.save(participation);

        this.eventInterface.updateEventAvaliation(event.getId(), getAvgStarsByEvent(event.getId()));

        UUID idEventOwner = event.getCreator().getId();
        UserResponseDTO userResponseDTO = getUserResponseDTO(avaliation);
        this.userInterface.updateUserAvaliation(idEventOwner, getAvgStarsByUser(idEventOwner));
        return new ResponseEvaluationDto(avaliation.getId(), avaliation.getStars(), avaliation.getComment(), userResponseDTO);
    }

    private static UserResponseDTO getUserResponseDTO(Evaluation avaliation) {
        User userEvaluation = avaliation.getUser();
        Photo photo = userEvaluation.getPhoto();
        PhotoDto photoDto = new PhotoDto(
                photo.getId(),
                photo.getExtension(),
                photo.isSaved()
        );
        return new UserResponseDTO(userEvaluation.getId(), userEvaluation.getUsername(),userEvaluation.getFullName(),
                userEvaluation.getBio(), userEvaluation.getInterests(), photoDto, userEvaluation.getStars());
    }

    @Override
    public Collection<ResponseEvaluationDto> getEventAvaliations(UUID idEvent) {
        Event event = this.eventInterface.findEvent(idEvent);
        this.eventInterface.checkUserParticipating(event);

        if (!(getParticipationFromEvent(event).getPosition().getRank() == 2)) {
            throw new InadequateUserPosition();
        }

        List<Evaluation> evaluations = this.participationRepository.getAvaliationByEventOrdered(event.getId())
                .stream()
                .toList();

        List<ResponseEvaluationDto> responseList = new ArrayList<>();

        for (Evaluation evaluation : evaluations) {
            UserResponseDTO userResponseDTO = getUserResponseDTO(evaluation);

            ResponseEvaluationDto responseDto = new ResponseEvaluationDto(
                    evaluation.getId(),
                    evaluation.getStars(),
                    evaluation.getComment(),
                    userResponseDTO
            );

            responseList.add(responseDto);
        }
        return responseList;
    }

    @Override
    public UserRateSituationDto hasUserAlreadyRatedTheEvent(UUID idParticipation) {
        User userLogged = getUserLogged.getUserLogged();
        Participation participation = participationRepository.findById(idParticipation)
                .orElseThrow(() -> new ParticipationNotFoundException("Participação não encontrada"));
        RateSituationEnum rateSituation = RateSituationEnum.NOT_RATED;

        if (!participation.getIdUser().equals(userLogged.getId())) {
            throw new UserIdConflictException();
        }

        if (participation.getEvaluation() != null) {
            rateSituation = RateSituationEnum.ALREADY_RATED;
        }

        return new UserRateSituationDto(rateSituation);
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
