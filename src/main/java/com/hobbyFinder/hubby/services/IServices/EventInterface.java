package com.hobbyFinder.hubby.services.IServices;

import com.hobbyFinder.hubby.exception.AuthException.Registro.CredenciaisRegistroException;
import com.hobbyFinder.hubby.models.dto.events.EventCreateDto;
import com.hobbyFinder.hubby.models.dto.events.EventDto;

public interface EventInterface {
    
    /**
     * Registra um novo evento definindo o usuário em sessão como criador.
     * 
     * @param eventCreateDto objeto dto com as informações essenciais para criar um novo evento
     * @return Objeto dto com as informações do evento criado
     * @throws CredenciaisRegistroException se as informações de criação não estiverem corretas
     */
    public EventDto registerEvent(EventCreateDto eventCreateDto);

}
