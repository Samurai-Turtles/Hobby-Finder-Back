package com.hobbyFinder.hubby.services.ServicesImpl;

import com.hobbyFinder.hubby.exception.NotFound.PageIsEmptyException;
import com.hobbyFinder.hubby.exception.NotFound.ParticipationNotFoundException;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.InadequateUserPosition;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.IncorrectEventIdParticipation;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.UserNotInEventException;
import com.hobbyFinder.hubby.models.dto.participations.GetResponseParticipationEvent;
import com.hobbyFinder.hubby.models.dto.participations.GetResponseParticipationsUser;
import com.hobbyFinder.hubby.models.dto.participations.ParticipationDto;
import com.hobbyFinder.hubby.models.dto.participations.UpdateParticipationDto;
import com.hobbyFinder.hubby.models.entities.Avaliation;
import com.hobbyFinder.hubby.models.entities.Participation;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.repositories.ParticipationRepository;
import com.hobbyFinder.hubby.services.IServices.ParticipationInterface;
import com.hobbyFinder.hubby.util.GetUserLogged;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipationServiceImpl implements ParticipationInterface {

    @Autowired
    private GetUserLogged getUserLogged;

    @Autowired
    private ParticipationRepository participationRepository;

    @Override
    public void deleteUserFromEvent(ParticipationDto participationDto) {
        Participation participation = findParticipation(participationDto.idParticipation());
        checkEventParticipation(participation.getIdEvent(), participationDto.idEvent());
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
        participationRepository.delete(participation);
        participationRepository.flush();
    }

    @Override
    public void updateParticipation(UpdateParticipationDto updateParticipationDTO) {
        Participation participation = findParticipation(updateParticipationDTO.idParticipation());
        checkEventParticipation(participation.getIdEvent(), updateParticipationDTO.idEvent());
        participation.setUserParticipation(updateParticipationDTO.participation());
        participationRepository.save(participation);
    }

    @Override
    public void deleteUserFromEvent(UUID idEvent, UUID idParticipation) {
        Participation participation = findParticipation(idParticipation);
        checkEventParticipation(participation.getIdEvent(), idEvent);
        User user = getUserLogged.getUserLogged();
        checkPositionInEvent(user, participation, idEvent);
        removeParticipation(idParticipation);
    }

    //essa função auxilia a lançar excecao de apenas um lugar e pode ser refatorada para quando houver uma possível
    //verificação de id usuário e id do evento.
    private void checkEventParticipation(UUID eventId, UUID participationEventId) {
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

        if (participationPage.hasContent()) {
            throw new PageIsEmptyException("A página indicada está vazia");
        }
        return participationPage.map(participation -> new GetResponseParticipationsUser(participation.getIdEvent(), participation.getUserParticipation()));
    }

    @Override
    public Page<GetResponseParticipationEvent> getParticipationEvents(UUID idEvent, Pageable pageable) {
        eventService.findEvent(idEvent);
        Page<Participation> participationsPage = participationRepository.findByIdEvent(idEvent, pageable);

        if (participationsPage.hasContent()) {
            throw new PageIsEmptyException("A página indicada esta vazia");
        }
        return participationsPage.map(participation -> new GetResponseParticipationEvent(participation.getIdUser(), participation.getUserParticipation()));
    }

    @Override
    public List<Avaliation> getAvaliationsFromEvent(UUID idEvent) {
        return this.participationRepository.getAvaliationByEventOrdered(idEvent);
    }

    @Override
    public void saveParticipation(Participation participation) {
        this.participationRepository.save(participation);
    }

    @Override
    public double getAvgStarsByEvent(UUID idEvent) {
        return this.participationRepository.avgStarsByEvent(idEvent);
    }

    @Override
    public double getAvgStarsByUser(UUID idUser) {
        return this.participationRepository.findAverageStarsByUser(idUser);
    }
}

