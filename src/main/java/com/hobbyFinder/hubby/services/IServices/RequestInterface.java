package com.hobbyFinder.hubby.services.IServices;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hobbyFinder.hubby.models.dto.participationRequest.ParticipationRequestEventDto;
import com.hobbyFinder.hubby.models.dto.participationRequest.ParticipationRequestUserDto;

public interface RequestInterface {

    /**
     * Cria uma nova solicitação para participação de um usuário em um evento
     * privado.
     * 
     * @param targetEventId - id válido do evento de interesse
     */
    void newRequest(UUID targetEventId);

    /**
     * Busca todas as solicitações de participação relacionadas a um evento privado.
     * 
     * @param targetEventId - id válido do evento de interesse
     * @param pageable      - um objeto que permite definir os padrões de paginação
     *                      da listagem
     */
    Page<ParticipationRequestEventDto> getAllEventRequests(UUID targetEventId, Pageable pageable);

    /**
     * Busca todas as solicitações de participação registradas por um usuário.
     * 
     * @param pageable - um objeto que permite definir os padrões de paginação
     *                 da listagem
     */
    Page<ParticipationRequestUserDto> getAllUserRequests(Pageable pageable);

    /**
     * Deleta uma solicitação criada previamente pelo usuário conectado.
     * 
     * @param targetRequestId - id da solicitação a ser deletada
     */
    void deleteRequestByUser(UUID targetRequestId);

    /**
     * Aceita uma solicitação de participação que está vinculada a um evento.
     * Uma solicitação só pode ser aceita por um usuário que faça parte da organização do evento
     * seja o administrador ou um organizador.
     * 
     * @param targetEventId - id do evento que recebeu a solicitação
     * @param targetRequestId - id da solicitação a ser aceita
     */
    void acceptRequest(UUID targetEventId, UUID targetRequestId);

    /**
     * Rejeita uma solicitação de participação que está vinculada a um evento.
     * Uma solicitação só pode ser rejeitada por um usuário que faça parte da organização do evento
     * seja o administrador ou um organizador.
     * 
     * @param targetEventId - id do evento que recebeu a solicitação
     * @param targetRequstId - id da solicitação a ser aceita
     */
    void declineRequest(UUID targetEventId, UUID targetRequstId);

}
