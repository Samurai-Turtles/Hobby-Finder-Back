package com.hobbyFinder.hubby.services.IServices;

import java.util.UUID;

public interface ParticipationRequestInterface {

    /**
     * Cria uma nova solicitação para participação de um usuário em um evento privado.
     * 
     * @param targetEventId - id válido do evento de interesse
     */
    void newParticipationRequest(UUID targetEventId);
    
}
