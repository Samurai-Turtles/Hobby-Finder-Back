package com.hobbyFinder.hubby.controllerTest.EventTests;

import com.hobbyFinder.hubby.models.dto.LocalDto;
import com.hobbyFinder.hubby.models.dto.events.EventDto;
import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.models.entities.Local;
import com.hobbyFinder.hubby.repositories.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

import java.util.Arrays;
import java.util.List;

@Component
public class EventSeeder implements CommandLineRunner {

    private final EventRepository eventRepository;

    public EventSeeder(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void run(String... args) {
        if (eventRepository.count() == 0) {
            List<EventDto> eventDtos = Arrays.asList(
                    EventConstants.EVENT_DTO_CREATE,
                    EventConstants.EVENT_DTO_UPDATE,
                    EventConstants.EVENT_DTO,
                    EventConstants.EVENT_DTO_SPECIAL,
                    EventConstants.EVENT_DTO_INVALID_DATE,
                    EventConstants.EVENT_DTO_INVALID
            );

            List<Event> events = eventDtos.stream()
                    .map(this::convertDtoToEntity) // Converte DTO para entidade
                    .collect(Collectors.toList());

            eventRepository.saveAll(events); // Salva os eventos no banco
            System.out.println("Eventos adicionados ao banco de dados!");
        }
    }

    private Event convertDtoToEntity(EventDto dto) {
        return Event.builder()
                .id(dto.id()) // UUID
                .name(dto.Name()) // Nome do evento
                .EventBegin(dto.begin()) // Data de início
                .EventEnd(dto.end()) // Data de término
                .local(convertLocalDtoToEntity(dto.local())) // Converte LocalDto para Local
                .privacy(dto.privacy()) // Privacidade
                .description(dto.description()) // Descrição
                .maxUserAmout(dto.MaxUserAmmount()) // Máximo de usuários
                .build();
    }

    private Local convertLocalDtoToEntity(LocalDto localDto) {
        if (localDto == null) return null;
        Local local = new Local();
        local.setStreet(localDto.street());
        local.setDistrict(localDto.district());
        local.setNumber(localDto.number());
        local.setCity(localDto.city());
        local.setState(localDto.state());
        local.setLatitude(localDto.latitude());
        local.setLongitude(localDto.longitude());
        return local;
    }
}
