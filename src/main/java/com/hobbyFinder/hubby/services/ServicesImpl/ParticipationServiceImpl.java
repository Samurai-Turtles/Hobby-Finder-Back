package com.hobbyFinder.hubby.services.ServicesImpl;

import com.hobbyFinder.hubby.exception.NotFound.PageIsEmptyException;
import com.hobbyFinder.hubby.exception.NotFound.ParticipationNotFoundException;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.InadequateUserPosition;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.IncorrectEventIdParticipation;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.UserIdConflictException;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.UserNotInEventException;
import com.hobbyFinder.hubby.models.dto.participations.*;
import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.models.entities.Participation;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.models.enums.PrivacyEnum;
import com.hobbyFinder.hubby.models.enums.UserParticipation;
import com.hobbyFinder.hubby.repositories.ParticipationRepository;
import com.hobbyFinder.hubby.services.IServices.EventInterface;
import com.hobbyFinder.hubby.services.IServices.ParticipationInterface;
import com.hobbyFinder.hubby.services.IServices.UserInterface;
import com.hobbyFinder.hubby.util.GetUserLogged;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ParticipationServiceImpl implements ParticipationInterface {

    @Autowired
    private EventInterface eventInterface;

    @Autowired
    private UserInterface userInterface;

    @Autowired
    private GetUserLogged getUserLogged;

    @Autowired
    private ParticipationRepository participationRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public void selfDeleteUserFromEvent(ParticipationDto participationDto) {
        eventInterface.findEvent(participationDto.idEvent());
        Participation participation = findParticipation(participationDto.idParticipation());
        checkEventParticipation(participation.getIdEvent(), participationDto.idEvent());
        if(!getUserLogged.getUserLogged().getId().equals(participation.getIdUser())) {
            throw new UserIdConflictException();
        }
        removeParticipation(participationDto.idParticipation());
    }

    @Override
    public Participation findParticipation(UUID participationId) {
        return participationRepository.findByIdParticipation(participationId)
                .orElseThrow(() -> new ParticipationNotFoundException("Participação não encontrada!"));
    }

    @Override
    public void removeParticipation(UUID participationId) {
        Participation participation = findParticipation(participationId);
        participationRepository.deleteUserParticipationsByParticipationId(participationId);
        participationRepository.deleteFromEventParticipations(participationId);
        participationRepository.delete(participation);
        participationRepository.flush();
    }

    @Override
    public void updateParticipation(UUID idEvent, UUID idParticipation, UserParticipation userParticipation) {
        Participation participation = findParticipation(idParticipation);
        checkEventParticipation(participation.getIdEvent(), idEvent);
        participation.setUserParticipation(userParticipation);
        participationRepository.save(participation);
        User user = userInterface.getUser(participation.getIdUser());
        Event event = eventInterface.findEvent(idEvent);
        notificationService.notifyConfirmParticipation(user, event, participation);
    }

    @Override
    public void deleteUserFromEvent(UUID idEvent, UUID idParticipation) {
        Participation participation = findParticipation(idParticipation);
        checkEventParticipation(participation.getIdEvent(), idEvent);
        User user = getUserLogged.getUserLogged();
        checkPositionInEvent(user, participation, idEvent);
        removeParticipation(idParticipation);
    }

    private void checkEventParticipation(UUID eventId, UUID participationEventId) {
        eventInterface.findEvent(participationEventId);

        if(!eventId.equals(participationEventId)) {
            throw new IncorrectEventIdParticipation();
        }
    }

    private void checkPositionInEvent(User user, Participation participationToDelete, UUID eventId) {
        Participation participation = user.getParticipations().stream()
                .filter(p -> p.getIdEvent().equals(eventId))
                .findFirst()
                .orElseThrow(UserNotInEventException::new);

        if(participation.getPosition().getRank() <= participationToDelete.getPosition().getRank()) {
            throw new InadequateUserPosition();
        }
    }

    @Override
    public Page<GetResponseParticipationsUser> getParticipationsUser(Pageable pageable) {
        User user = getUserLogged.getUserLogged();
        Page<Participation> participationPage = participationRepository.findByUserId(user.getId(), pageable);

        if (!participationPage.hasContent()) {
            throw new PageIsEmptyException("A página indicada está vazia");
        }
        return participationPage.map(participation -> new GetResponseParticipationsUser(participation.getIdEvent(), participation.getUserParticipation()));
    }

    @Override
    public Page<GetResponseParticipationEvent> getParticipationEvents(UUID idEvent, Pageable pageable) {
        Event event = eventInterface.findEvent(idEvent);
        if (event.getPrivacy().equals(PrivacyEnum.PRIVATE)) {
            eventInterface.checkUserParticipating(event);
        }
        Page<Participation> participationsPage = participationRepository.findByIdEvent(idEvent, pageable);

        if (!participationsPage.hasContent()) {
            throw new PageIsEmptyException("A página indicada esta vazia");
        }
        return participationsPage.map(participation -> new GetResponseParticipationEvent(participation.getIdParticipation(),participation.getIdUser(), participation.getUserParticipation()));
    }

    @Override
    public UpdateParticipationDto participationManagement(UUID idEvent, UUID idParticipation, UpdateParticipationDto updateParticipationDTO) {
        Participation participation = findParticipation(idParticipation);
        eventInterface.findEvent(idEvent);
        User user = getUserLogged.getUserLogged();
        Participation myParticipation = user.getParticipations().stream()
                .filter(p -> p.getIdEvent().equals(idEvent))
                .findFirst()
                .orElseThrow(() -> new ParticipationNotFoundException("Participação não encontrada."));
        if(participation.getPosition().getRank() >= myParticipation.getPosition().getRank()) {
            throw new InadequateUserPosition();
        }
        participation.setPosition(updateParticipationDTO.position());
        participation.setUserParticipation(updateParticipationDTO.participation());
        participationRepository.save(participation);
        return new UpdateParticipationDto(participation.getUserParticipation(), participation.getPosition());
    }

}