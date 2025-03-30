package com.hobbyFinder.hubby.services.ServicesImpl;

import com.hobbyFinder.hubby.exception.NotFound.EventNotFoundException;
import com.hobbyFinder.hubby.models.dto.situations.SituationDto;
import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.models.entities.Participation;
import com.hobbyFinder.hubby.models.entities.ParticipationRequest;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.repositories.ParticipationRepository;
import com.hobbyFinder.hubby.repositories.RequestRepository;
import com.hobbyFinder.hubby.repositories.UserRepository;
import com.hobbyFinder.hubby.services.IServices.SituationInterface;
import com.hobbyFinder.hubby.services.helpers.ConstantsSituations;
import com.hobbyFinder.hubby.util.GetUserLogged;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class SituationService implements SituationInterface {

    private final UserRepository userRepository;
    private ParticipationRepository participationRepository;
    private RequestRepository requestRepository;
    private EventRepository eventRepository;

    private GetUserLogged getUserLogged;

    private SituationDto getSituation(User user, UUID eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);

        if (event == null) {
            throw new EventNotFoundException("Evento não cadastrado");
        }

        Stream<ParticipationRequest> requests = event.getRequests().stream();
        Stream<Participation>  participations = event.getParticipations().stream();

        requests = requests.filter(participationRequest -> participationRequest.getUser().getId().equals(user.getId()));
        participations = participations.filter(participation -> participation.getIdUser().equals(user.getId()));

        Participation  participation = participations.findFirst().orElse(null);
        ParticipationRequest request = requests.findFirst().orElse(null);

        if (participation != null) {
            if (participation.isConfirmed())
            {
                return ConstantsSituations.CONFIRMED;
            }
            return ConstantsSituations.NOT_CONFIRMED;
        }

        if (request != null) {
            return ConstantsSituations.NOT_ACCEPTED;
        }

        return ConstantsSituations.NOT_THERE;
    }

    @Override
    public SituationDto getSituationByAuthUser(UUID eventId) {
        User user = getUserLogged.getUserLogged();
        return getSituation(user, eventId);

    }
}
