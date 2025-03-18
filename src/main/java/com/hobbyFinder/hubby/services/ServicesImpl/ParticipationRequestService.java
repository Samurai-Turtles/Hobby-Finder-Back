package com.hobbyFinder.hubby.services.ServicesImpl;

import java.util.UUID;

import org.springframework.stereotype.Service;

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
        } else {
            // Generate a new Participation Object
        }
    }

}
