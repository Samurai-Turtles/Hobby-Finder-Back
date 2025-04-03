package com.hobbyFinder.hubby.services.ServicesImpl;

import com.hobbyFinder.hubby.exception.NotFound.EventNotFoundException;
import com.hobbyFinder.hubby.models.dto.situations.SituationDto;
import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.models.entities.Participation;
import com.hobbyFinder.hubby.models.entities.ParticipationRequest;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.models.enums.ParticipationPosition;
import com.hobbyFinder.hubby.models.enums.SituationEnum;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.repositories.RequestRepository;
import com.hobbyFinder.hubby.services.IServices.SituationInterface;
import com.hobbyFinder.hubby.util.GetUserLogged;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class SituationService implements SituationInterface {

    private EventRepository eventRepository;
    private RequestRepository requestRepository;
    private GetUserLogged getUserLogged;

    private SituationDto getSituation(User user, UUID eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);

        if (event == null) {
            throw new EventNotFoundException("Evento não cadastrado");
        }

        Stream<ParticipationRequest> requests = requestRepository
                .findByEvent(event, Pageable.ofSize(event.getMaxUserAmount()))
                .getContent()
                .stream();
        Stream<Participation>  participations = event.getParticipations().stream();

        requests = requests.filter(participationRequest -> participationRequest.getUser().getId().equals(user.getId()));
        participations = participations.filter(participation -> participation.getIdUser().equals(user.getId()));

        Participation  participation = participations.findFirst().orElse(null);
        ParticipationRequest request = requests.findFirst().orElse(null);

        if (participation != null) {
            if (participation.getPosition().equals(ParticipationPosition.CREATOR)){
                return new SituationDto(SituationEnum.CRIADOR, participation.getIdParticipation(), null);
            }

            if (participation.getPosition().equals(ParticipationPosition.ADMIN)){
                return new SituationDto(SituationEnum.ADM, participation.getIdParticipation(), null);
            }

            if (participation.isConfirmed())
            {
                return new SituationDto(SituationEnum.PARTICIPANTE_CONFIRMADO, participation.getIdParticipation(), null);
            }
            return new SituationDto(SituationEnum.PARTICIPANTE_NAO_CONFIRMADO, participation.getIdParticipation(), null);
        }

        if (request != null) {
            return new SituationDto(SituationEnum.SOLICITANTE, null, request.getId());
        }

        return new SituationDto(SituationEnum.NAO_PARTICIPANTE, null, null);
    }

    @Override
    public SituationDto getSituationByAuthUser(UUID eventId) {
        User user = getUserLogged.getUserLogged();
        return getSituation(user, eventId);
    }
}
