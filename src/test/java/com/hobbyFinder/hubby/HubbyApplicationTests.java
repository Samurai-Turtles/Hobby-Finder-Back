package com.hobbyFinder.hubby;

import com.hobbyFinder.hubby.models.dto.events.EventCreateDto;
import com.hobbyFinder.hubby.models.dto.events.EventDto;
import com.hobbyFinder.hubby.models.dto.LocalDto;
import com.hobbyFinder.hubby.models.enums.PrivacyEnum;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.services.ServicesImpl.EventService;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Transactional
@DisplayName("Testes para EventService")
public class HubbyApplicationTests {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private EventCreateDto eventCreateDto;

    @BeforeEach
    void setUp() {
        // Configuração do EventCreateDto para os testes
        eventCreateDto = new EventCreateDto(
                "Festa de Aniversário",
                LocalDateTime.of(2023, 12, 25, 18, 0),
                LocalDateTime.of(2023, 12, 25, 23, 0),
                new LocalDto(
                        -23.5505, // latitude
                        -46.6333, // longitude
                        "Rua das Flores", // rua
                        "Centro", // bairro
                        "123", // numero
                        "São Paulo", // cidade
                        "SP" // estado
                ),
                PrivacyEnum.PUBLIC,
                "Venha celebrar meu aniversário!",
                50
        );
    }

    @Test
    @DisplayName("Registrar evento com sucesso")
    void testRegistrarEventoComSucesso() {
        // Executa o método a ser testado
        EventDto eventDto = eventService.registerEvent(eventCreateDto);

        // Verificações
        assertNotNull(eventDto);
        assertEquals(eventCreateDto.Name(), eventDto.Name());
        assertEquals(eventCreateDto.begin(), eventDto.begin());
        assertEquals(eventCreateDto.end(), eventDto.end());
        assertEquals(eventCreateDto.local(), eventDto.local());
        assertEquals(eventCreateDto.privacy(), eventDto.privacy());
        assertEquals(eventCreateDto.description(), eventDto.description());
        assertEquals(eventCreateDto.MaxUserAmmount(), eventDto.MaxUserAmmount());
        assertEquals(0, eventDto.userCount()); // userCount deve ser 0 inicialmente
    }

    @Test
    @DisplayName("Registrar evento com dados nulos")
    void testRegistrarEventoComDadosNulos() {
        // Testa o caso em que o EventCreateDto é nulo
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            eventService.registerEvent(null);
        });

        // Verifica a mensagem de erro
        assertEquals("Os dados do evento não podem ser nulos.", exception.getMessage());
    }

    @Test
    @DisplayName("Registrar evento com nome nulo")
    void testRegistrarEventoComNomeNulo() {
        // Configura um EventCreateDto com nome nulo
        EventCreateDto eventCreateDtoNomeNulo = new EventCreateDto(
                null, // Nome nulo
                LocalDateTime.of(2023, 12, 25, 18, 0),
                LocalDateTime.of(2023, 12, 25, 23, 0),
                new LocalDto(
                        -23.5505, // latitude
                        -46.6333, // longitude
                        "Rua das Flores", // rua
                        "Centro", // bairro
                        "123", // numero
                        "São Paulo", // cidade
                        "SP" // estado
                ),
                PrivacyEnum.PUBLIC,
                "Venha celebrar meu aniversário!",
                50
        );

        // Testa o caso em que o nome é nulo
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            eventService.registerEvent(eventCreateDtoNomeNulo);
        });

        // Verifica a mensagem de erro
        assertEquals("O nome do evento não pode ser nulo.", exception.getMessage());
    }

    @Test
    @DisplayName("Registrar evento com data de início no passado")
    void testRegistrarEventoComDataInicioNoPassado() {
        // Configura um EventCreateDto com data de início no passado
        EventCreateDto eventCreateDtoDataInvalida = new EventCreateDto(
                "Festa de Aniversário",
                LocalDateTime.of(2020, 12, 25, 18, 0), // Data no passado
                LocalDateTime.of(2023, 12, 25, 23, 0),
                new LocalDto(
                        -23.5505, // latitude
                        -46.6333, // longitude
                        "Rua das Flores", // rua
                        "Centro", // bairro
                        "123", // numero
                        "São Paulo", // cidade
                        "SP" // estado
                ),
                PrivacyEnum.PUBLIC,
                "Venha celebrar meu aniversário!",
                50
        );

        // Testa o caso em que a data de início é no passado
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            eventService.registerEvent(eventCreateDtoDataInvalida);
        });

        // Verifica a mensagem de erro
        assertEquals("A data de início do evento deve ser no futuro.", exception.getMessage());
    }
}
