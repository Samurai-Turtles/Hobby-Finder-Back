package com.hobbyFinder.hubby.services.ServicesImpl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hobbyFinder.hubby.models.dto.events.EventCreateDto;
import com.hobbyFinder.hubby.models.dto.events.EventDto;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.services.IServices.EventInterface;

@Service
public class EventService implements EventInterface{

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public EventDto registerEvent(EventCreateDto eventCreateDto) {
        if (eventCreateDto == null) {
            throw new IllegalArgumentException("Os dados do evento não podem ser nulos.");
        }
    
        // Criar um EventDto a partir do EventCreateDto
        EventDto eventDto = new EventDto(
                UUID.randomUUID(), // Gera um novo ID único
                eventCreateDto.Name(), // Nome do evento
                eventCreateDto.begin(), // Data de início
                eventCreateDto.end(), // Data de término
                eventCreateDto.local(), // Local do evento
                eventCreateDto.privacy(), // Nível de privacidade
                eventCreateDto.description(), // Descrição do evento
                eventCreateDto.MaxUserAmmount(), // Quantidade máxima de usuários
                0 // Inicialmente o número de usuários é 0
        );
    
        return eventDto;
    }
}
