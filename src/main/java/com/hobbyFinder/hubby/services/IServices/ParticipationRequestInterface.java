package com.hobbyFinder.hubby.services.IServices;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hobbyFinder.hubby.models.entities.ParticipationRequest;

public interface ParticipationRequestInterface {

    /**
     * Cria uma nova solicitação para participação de um usuário em um evento
     * privado.
     * 
     * @param targetEventId - id válido do evento de interesse
     */
    void newParticipationRequest(UUID targetEventId);

    /**
     * Busca todas as solicitações de participação relacionadas a um evento privado.
     * 
     * @param targetEventId - id válido do evento de interesse
     * @param pageable      - um objeto que permite definir os padrões de paginação
     *                      da listagem
     */
    Page<ParticipationRequest> getAllEventRequests(UUID targetEventId, Pageable pageable);

    /**
     * Busca todas as solicitações de participação registradas por um usuário.
     * 
     * @param pageable - um objeto que permite definir os padrões de paginação
     *                 da listagem
     */
    Page<ParticipationRequest> getAllUserRequests(Pageable pageable);

    /**
     * Deleta uma solicitação criada previamente pelo usuário conectado.
     * 
     * @param targetRequestId - id da solicitação a ser deletada
     */
    void deleteParticipationRequestByUser(UUID targetRequestId);

    /**
     * Aceita uma solicitação de participação que está vinculada a um evento.
     * Uma solicitação só pode ser aceita por um usuário que faça parte da organização do evento
     * seja o administrador ou um organizador.
     * 
     * @param targetEvent - id do evento que recebeu a solicitação
     * @param targetRequestId - id da solicitação a ser aceita
     */
    void acceptRequest(UUID targetEvent, UUID targetRequestId);

}
