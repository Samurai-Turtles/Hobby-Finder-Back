package com.hobbyFinder.hubby.services.IServices;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hobbyFinder.hubby.models.entities.ParticipationRequest;

public interface ParticipationRequestInterface {

    /**
     * Cria uma nova solicitação para participação de um usuário em um evento privado.
     * 
     * @param targetEventId - id válido do evento de interesse
     */
    void newParticipationRequest(UUID targetEventId);

    /**
     * Busca todas as solicitações de participação relacionadas a um evento privado.
     * 
     * @param targetEventId - id válido do evento de interesse
     * @param pageable - um objeto que permite definir os padrões de paginação da listagem
     */
    Page<ParticipationRequest> getAllEventRequests(UUID targetEventId, Pageable pageable);
    
}
