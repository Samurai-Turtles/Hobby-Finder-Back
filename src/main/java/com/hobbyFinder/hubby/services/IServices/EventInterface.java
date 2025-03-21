package com.hobbyFinder.hubby.services.IServices;

import com.hobbyFinder.hubby.exception.AuthException.Registro.CredenciaisRegistroException;
import com.hobbyFinder.hubby.exception.NotFound.EventNotFoundException;
import com.hobbyFinder.hubby.models.dto.events.EventCreateDto;
import com.hobbyFinder.hubby.models.dto.events.EventDto;
import com.hobbyFinder.hubby.models.dto.participations.GetResponseParticipationEvent;
import com.hobbyFinder.hubby.models.entities.Event;

import java.util.List;
import java.util.UUID;

public interface EventInterface {
    
    /**
     * Registra um novo evento definindo o usuário em sessão como criador.
     * 
     * @param eventCreateDto objeto dto com as informações essenciais para criar um novo evento
     * @return Objeto dto com as informações do evento criado
     * @throws CredenciaisRegistroException se as informações de criação não estiverem corretas
     */
    public EventDto registerEvent(EventCreateDto eventCreateDto);
    public List<GetResponseParticipationEvent> getParticipationsEvent(UUID idEvent);
    public Event findEvent(UUID idEvent);

    /**
     * Deleta um evento pelo UUID
     * @param uuid
     * @throws
     */
    public void deleteEvent(UUID uuid) throws EventNotFoundException;
}
