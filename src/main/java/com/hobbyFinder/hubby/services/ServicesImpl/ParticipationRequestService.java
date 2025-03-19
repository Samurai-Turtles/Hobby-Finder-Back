package com.hobbyFinder.hubby.services.ServicesImpl;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hobbyFinder.hubby.exception.EntityStateException.EventIsPublicException;
import com.hobbyFinder.hubby.exception.EntityStateException.NonOwnerUserException;
import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.models.entities.ParticipationRequest;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.repositories.ParticipationRequestRepository;
import com.hobbyFinder.hubby.services.IServices.ParticipationRequestInterface;
import com.hobbyFinder.hubby.util.GetUserLogged;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ParticipationRequestService implements ParticipationRequestInterface {

    private GetUserLogged getUserLogged;
    private EventRepository eventRepository;
    private ParticipationRequestRepository requestRepository;

    @Override
    public void newParticipationRequest(UUID targetEventId) {
        User userLogged = getUserLogged.getUserLogged();
        Event targetEvent = eventRepository.getReferenceById(targetEventId);

        if (targetEvent.getPrivacy().name().equals("PRIVATE")) {
            ParticipationRequest newRequest = ParticipationRequest.builder()
                    .event(targetEvent)
                    .user(userLogged)
                    .build();

            userLogged.getRequests().add(newRequest);
            requestRepository.save(newRequest);
            requestRepository.flush();
        } else {
            // Generate a new Participation Object
        }
    }

    @Override
    public Page<ParticipationRequest> getAllEventRequests(UUID targetEventUuid, Pageable pageable) {
        Event targetEvent = eventRepository.getReferenceById(targetEventUuid);

        if (targetEvent.getPrivacy().name().equals("PUBLIC")) {
            throw new EventIsPublicException("Eventos públicos não possuem lista de solicitações");
        }

        return requestRepository.findByEvent(targetEvent, pageable);
    }

    @Override
    public Page<ParticipationRequest> getAllUserRequests(Pageable pageable) {
        User userLogged = getUserLogged.getUserLogged();
        return requestRepository.findByUser(userLogged, pageable);
    }

    @Override
    public void deleteParticipationRequestByUser(UUID targetRequestId) {
        User userLogged = getUserLogged.getUserLogged();
        ParticipationRequest targetRequest = requestRepository.getReferenceById(targetRequestId);

        if(!targetRequest.getUser().equals(userLogged)) {
            throw new NonOwnerUserException("Usuário não possui a requisição informada");
        }

        requestRepository.delete(targetRequest);
    }

}
