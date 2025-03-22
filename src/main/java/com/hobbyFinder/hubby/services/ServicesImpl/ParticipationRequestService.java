package com.hobbyFinder.hubby.services.ServicesImpl;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hobbyFinder.hubby.exception.EntityStateException.EventIsPublicException;
import com.hobbyFinder.hubby.exception.EntityStateException.NonOwnerUserException;
import com.hobbyFinder.hubby.exception.NotFound.NotFoundException;
import com.hobbyFinder.hubby.exception.NotFound.PageIsEmptyException;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.InadequateUserPosition;
import com.hobbyFinder.hubby.models.dto.participationRequest.EventRequestResponse;
import com.hobbyFinder.hubby.models.dto.participationRequest.ParticipationRequestEventDto;
import com.hobbyFinder.hubby.models.dto.participationRequest.ParticipationRequestUserDto;
import com.hobbyFinder.hubby.models.dto.participationRequest.UserRequestResponse;
import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.models.entities.Participation;
import com.hobbyFinder.hubby.models.entities.ParticipationRequest;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.models.enums.ParticipationPosition;
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
    public Page<ParticipationRequestEventDto> getAllEventRequests(UUID targetEventUuid, Pageable pageable) {
        Event targetEvent = eventRepository.getReferenceById(targetEventUuid);

        if (targetEvent.getPrivacy().name().equals("PUBLIC")) {
            throw new EventIsPublicException("Eventos públicos não possuem lista de solicitações");
        }

        Page<ParticipationRequest> out = requestRepository.findByEvent(targetEvent, pageable);

        if (out.getContent().isEmpty()) {
            throw new PageIsEmptyException("A página indicada está vazia");
        }

        return out.map(request -> new ParticipationRequestEventDto(request.getId(),
                mappingToEventResponse(request.getEvent())));
    }

    @Override
    public Page<ParticipationRequestUserDto> getAllUserRequests(Pageable pageable) {
        User userLogged = getUserLogged.getUserLogged();
        Page<ParticipationRequest> out = requestRepository.findByUser(userLogged, pageable);

        if (out.getContent().isEmpty()) {
            throw new PageIsEmptyException("A página indicada está vazia");
        }

        return out.map(
                request -> new ParticipationRequestUserDto(request.getId(), mappingToUserResponse(request.getUser())));
    }

    @Override
    public void deleteParticipationRequestByUser(UUID targetRequestId) {
        User userLogged = getUserLogged.getUserLogged();
        ParticipationRequest targetRequest = requestRepository.getReferenceById(targetRequestId);

        if (!targetRequest.getUser().equals(userLogged)) {
            throw new NonOwnerUserException("Usuário não possui a requisição informada");
        }

        requestRepository.delete(targetRequest);
    }

    @Override
    public void acceptRequest(UUID targetEvent, UUID targetRequestId) {
        checkUserPositionInEvent(targetEvent);

        // Generate a new Participation
        ParticipationRequest targetRequest = requestRepository.getReferenceById(targetRequestId);
        requestRepository.delete(targetRequest);
    }

    @Override
    public void declineRequest(UUID targetEventId, UUID targetRequstId) {
        checkUserPositionInEvent(targetEventId);

        ParticipationRequest targetRequest = requestRepository.getReferenceById(targetEventId);
        requestRepository.delete(targetRequest);
    }

    /**
     * Checa se o usuário tem cargo de gerência
     * 
     * @param targetEventId - representa o id do evento alvo
     */
    private void checkUserPositionInEvent(UUID targetEventId) {
        User userLogged = getUserLogged.getUserLogged();

        Participation targetParticipation = userLogged.getParticipations().stream()
                .filter(participation -> participation.getIdEvent() == targetEventId).findFirst()
                .orElseThrow(() -> new NotFoundException("Usuário não participa do evento informado."));

        if (targetParticipation.getPosition().equals(ParticipationPosition.PARTICIPANT)) {
            throw new InadequateUserPosition();
        }
    }

    /**
     * Cria um objeto de resposta relativo a um evento vinculado a uma solicitação
     * de participação.
     * 
     * @param event - objeto evento presente na entidade solicitação
     * @return um objeto resposta que representa os dados de um Evento
     */
    private EventRequestResponse mappingToEventResponse(Event event) {
        return new EventRequestResponse(event.getId(), event.getName(), event.getEventBegin(), event.getEventEnd(),
                event.getPrivacy().name(), event.getMaxUserAmout(),
                event.getParticipations().size());
    }

    /**
     * Cria um objeto de resposta relativo a um usuário vinculado a uma solicitação
     * de participação.
     * 
     * @param event - objeto usuário presente na entidade solicitação
     * @return um objeto resposta que representa os dados de um usuário
     */
    private UserRequestResponse mappingToUserResponse(User user) {
        return new UserRequestResponse(user.getId(), user.getUsername(), user.getBio());
    }

}
