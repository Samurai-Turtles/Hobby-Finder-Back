package com.hobbyFinder.hubby.services.ServicesImpl;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hobbyFinder.hubby.exception.EntityStateException.EventIsPublicException;
import com.hobbyFinder.hubby.exception.EntityStateException.InvalidPositionParticipateRequest;
import com.hobbyFinder.hubby.exception.EntityStateException.NonOwnerUserException;
import com.hobbyFinder.hubby.exception.EventException.EventCrowdedException;
import com.hobbyFinder.hubby.exception.NotFound.NotFoundException;
import com.hobbyFinder.hubby.exception.NotFound.PageIsEmptyException;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.InadequateUserPosition;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.UserAlreadyInEventException;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.UserNotInEventException;
import com.hobbyFinder.hubby.models.dto.participationRequest.EventRequestResponse;
import com.hobbyFinder.hubby.models.dto.participationRequest.ParticipationRequestEventDto;
import com.hobbyFinder.hubby.models.dto.participationRequest.ParticipationRequestUserDto;
import com.hobbyFinder.hubby.models.dto.participationRequest.UserRequestResponse;
import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.models.entities.Participation;
import com.hobbyFinder.hubby.models.entities.ParticipationRequest;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.models.enums.ParticipationPosition;
import com.hobbyFinder.hubby.models.enums.UserParticipation;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.repositories.ParticipationRepository;
import com.hobbyFinder.hubby.repositories.ParticipationRequestRepository;
import com.hobbyFinder.hubby.repositories.UserRepository;
import com.hobbyFinder.hubby.services.IServices.ParticipationRequestInterface;
import com.hobbyFinder.hubby.util.GetUserLogged;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ParticipationRequestService implements ParticipationRequestInterface {

    private GetUserLogged getUserLogged;
    private EventRepository eventRepository;
    private UserRepository userRepository;
    private ParticipationRequestRepository requestRepository;
    private ParticipationRepository participationRepository;
    private NotificationService notificationService;

    @Override
    public void newParticipationRequest(UUID targetEventId) {
        User userLogged = getUserLogged.getUserLogged();
        Event targetEvent = eventRepository.getReferenceById(targetEventId);
        boolean userAlreadyParticipate = userAlreadyParticipate(userLogged, targetEvent);

        if (!userAlreadyParticipate && targetEvent.getPrivacy().name().equals("PRIVATE")) {
            checkEventCapacity(targetEventId);

            ParticipationRequest newRequest = ParticipationRequest.builder()
                    .event(targetEvent)
                    .user(userLogged)
                    .build();

            userLogged.getRequests().add(newRequest);
            userRepository.save(userLogged);

            requestRepository.save(newRequest);
            requestRepository.flush();

            notificationService.notifySolicitation(userLogged, targetEvent);

        } else if (!userAlreadyParticipate) {
            checkEventCapacity(targetEventId);

            Participation newParticipation = createParticipationObject(targetEventId, userLogged.getId());

            participationRepository.save(newParticipation);
            participationRepository.flush();

            userLogged.getParticipations().add(newParticipation);
            targetEvent.getParticipations().add(newParticipation);

            userRepository.save(userLogged);
            eventRepository.save(targetEvent);
        }
    }

    @Override
    public Page<ParticipationRequestEventDto> getAllEventRequests(UUID targetEventUuid, Pageable pageable) {
        Event targetEvent = eventRepository.getReferenceById(targetEventUuid);

        if (targetEvent.getPrivacy().name().equals("PUBLIC")) {
            throw new EventIsPublicException("Eventos públicos não possuem lista de solicitações");
        }

        checkUserPositionInEvent(targetEventUuid);

        Page<ParticipationRequest> out = requestRepository.findByEvent(targetEvent, pageable);

        if (out.getContent().isEmpty()) {
            throw new PageIsEmptyException("A página indicada está vazia");
        }

        return out.map(request -> new ParticipationRequestEventDto(request.getId(),
                mappingToUserResponse(request.getUser())));
    }

    @Override
    public Page<ParticipationRequestUserDto> getAllUserRequests(Pageable pageable) {
        User userLogged = getUserLogged.getUserLogged();
        Page<ParticipationRequest> out = requestRepository.findByUser(userLogged, pageable);

        if (out.getContent().isEmpty()) {
            throw new PageIsEmptyException("A página indicada está vazia");
        }

        return out.map(
                request -> new ParticipationRequestUserDto(request.getId(), mappingToEventResponse(request.getEvent())));
    }

    @Override
    public void deleteParticipationRequestByUser(UUID targetRequestId) {
        User userLogged = getUserLogged.getUserLogged();
        ParticipationRequest targetRequest = requestRepository.findById(targetRequestId).orElse(null);

        if(targetRequest == null) {
            throw new NotFoundException("Requisição não encontrada.");
        }

        if (!targetRequest.getUser().equals(userLogged)) {
            throw new NonOwnerUserException("Usuário não possui a requisição informada");
        }

        requestRepository.delete(targetRequest);
    }

    @Override
    public void acceptRequest(UUID targetEventId, UUID targetRequestId) {
        checkEventCapacity(targetEventId);
        checkUserPositionInEvent(targetEventId);

        ParticipationRequest targetRequest = requestRepository.getReferenceById(targetRequestId);
        Participation newParticipation = createParticipationObject(targetEventId, targetRequest.getUser().getId());

        Event targetEvent = eventRepository.getReferenceById(targetEventId);
        targetRequest.getUser().getParticipations().add(newParticipation);
        targetEvent.getParticipations().add(newParticipation);

        eventRepository.save(targetEvent);
        userRepository.save(targetRequest.getUser());

        participationRepository.save(newParticipation);
        participationRepository.flush();

        requestRepository.delete(targetRequest);
        requestRepository.flush();

    }

    @Override
    public void declineRequest(UUID targetEventId, UUID targetRequstId) {
        eventRepository.findById(targetEventId).orElseThrow(() -> new NotFoundException("Evento não encontrado."));
        checkUserPositionInEvent(targetEventId);

        ParticipationRequest targetRequest = requestRepository.findById(targetRequstId).orElse(null);
        
        if(targetRequest == null) {
            throw new NotFoundException("Requisição não encontrada.");
        }

        requestRepository.delete(targetRequest);
    }

    /**
     * Checa se o evento está com capacidade máxima.
     * 
     * @param targetEventId - id do evento a checar
     */
    private void checkEventCapacity(UUID targetEventId) {
        Event targetEvent = eventRepository.findById(targetEventId).orElseThrow(() -> new NotFoundException("Evento não encontrado."));

        if (targetEvent.getParticipations().size() >= targetEvent.getMaxUserAmount()) {
            throw new EventCrowdedException();
        }
    }

    /**
     * Checa se o usuário logado já possui uma solicitação de participação ou se ele
     * já participa do evento.
     * 
     * @param userLogged  - objeto que representa o usuário logado
     * @param targetEvent - objeto que representa o evento de interesse
     * @return - true, para um usuário que já participa ou tem uma requisição
     *         criada; false, para um usuário que não participa nem tem uma
     *         requisição criada.
     */
    private boolean userAlreadyParticipate(User userLogged, Event targetEvent) {
        ParticipationRequest possibleRequest = requestRepository.findByUser(userLogged).stream()
                .filter(request -> !request.equals(null) && request.getEvent().equals(targetEvent)).findFirst()
                .orElse(null);

        Participation possibleParticipation = userLogged.getParticipations().stream()
                .filter(participation -> participation.getIdEvent() != null
                        && participation.getIdEvent().equals(targetEvent.getId()))
                .findFirst()
                .orElse(null);

        if(possibleParticipation != null) {
            throw new UserAlreadyInEventException();
        }

        return possibleRequest != null ;
    }

    /**
     * Cria um objeto que representa a participação de um usuário em um evento.
     * 
     * @param targetEvent - o id do evento de interesse
     * @param targetUser  - o id do usuário a participar do evento
     * @return - um objeto participação
     */
    private Participation createParticipationObject(UUID targetEvent, UUID targetUser) {
        Participation newParticipation = Participation.builder().idEvent(targetEvent).idUser(targetUser)
                .userParticipation(UserParticipation.UNCONFIRMED_PRESENCE).position(ParticipationPosition.PARTICIPANT)
                .build();

        return newParticipation;
    }

    /**
     * Checa se o usuário tem cargo de gerência
     * 
     * @param targetEventId - representa o id do evento alvo
     */
    private void checkUserPositionInEvent(UUID targetEventId) {
        User userLogged = getUserLogged.getUserLogged();

        Participation targetParticipation = userLogged.getParticipations().stream()
                .filter(participation -> participation.getIdEvent().equals(targetEventId)).findFirst()
                .orElse(null);

        if (targetParticipation == null || targetParticipation.getPosition().equals(ParticipationPosition.PARTICIPANT)) {
            throw new InvalidPositionParticipateRequest();
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
                event.getPrivacy().name(), event.getMaxUserAmount(),
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
