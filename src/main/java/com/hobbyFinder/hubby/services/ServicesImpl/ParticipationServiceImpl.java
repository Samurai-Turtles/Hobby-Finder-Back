package com.hobbyFinder.hubby.services.ServicesImpl;

import com.hobbyFinder.hubby.exception.NotFound.ParticipationNotFoundException;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.IncorrectEventIdParticipation;
import com.hobbyFinder.hubby.models.dto.events.ParticipationDto;
import com.hobbyFinder.hubby.models.dto.events.UpdateParticipationDto;
import com.hobbyFinder.hubby.models.entities.Participation;
import com.hobbyFinder.hubby.repositories.ParticipationRepository;
import com.hobbyFinder.hubby.services.IServices.ParticipationInterface;
import com.hobbyFinder.hubby.util.GetUserLogged;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ParticipationServiceImpl implements ParticipationInterface {

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

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

    //essa função auxilia a lançar excecao de apenas um lugar e pode ser refatorada para quando houver uma possível
    //verificação de id usuário.
    private void checkEventParticipation(UUID eventId, UUID participationEventId) {
        if(!eventId.equals(participationEventId)) {
            throw new IncorrectEventIdParticipation();
        }
    }
}

