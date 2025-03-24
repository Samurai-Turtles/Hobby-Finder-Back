package com.hobbyFinder.hubby.controllerTest.EventTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.controller.routes.EventRoutes;
import com.hobbyFinder.hubby.controllerTest.UserTests.UserSeeder;
import com.hobbyFinder.hubby.models.dto.events.EventPutDto;
import com.hobbyFinder.hubby.models.dto.events.LocalDto;
import com.hobbyFinder.hubby.models.enums.PrivacyEnum;
import com.hobbyFinder.hubby.repositories.EventRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes de rota: atualização de evento")
public class UpdateTest {

    @Autowired
    private MockMvc driver;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserSeeder userSeeder;
    @Autowired
    private EventSeeder eventSeeder;
    @Autowired
    private EventRepository eventRepository;

    private String token;
    private UUID eventId;

    @BeforeEach
    void setUp() throws Exception {
        userSeeder = new UserSeeder(driver, objectMapper);
        userSeeder.seedUsers();

        eventSeeder = new EventSeeder(driver, objectMapper, userSeeder);
        eventSeeder.seedEvents();
        this.token = eventSeeder.token;
        this.eventId = getEventId();
    }

    @AfterEach
    void tearDown() throws Exception {}

    private UUID getEventId() {
        return eventRepository.findAll().get(0).getId();
    }

    @Test
    @DisplayName("Atualiza um evento com sucesso")
    void testUpdateEventSuccess() throws Exception {
        EventPutDto eventPutDto = new EventPutDto(
                "Novo Nome",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                new LocalDto("Nova Rua", "Novo Bairro", "123", "Nova Cidade", "Novo Estado", 40, 70),
                PrivacyEnum.PUBLIC,
                "Nova Descrição",
                10
        );

        driver.perform(put(EventRoutes.PUT_EVENT, eventId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventPutDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Tenta atualizar um evento com ID inexistente")
    void testUpdateEventNotFound() throws Exception {
        UUID nonExistentEventId = UUID.randomUUID();

        EventPutDto eventPutDto = new EventPutDto(
                "Novo Nome",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                new LocalDto("Nova Rua", "Novo Bairro", "123", "Nova Cidade", "Novo Estado", 40, 70),
                PrivacyEnum.PUBLIC,
                "Nova Descrição",
                10
        );

        driver.perform(put(EventRoutes.PUT_EVENT, nonExistentEventId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventPutDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Tenta atualizar a capacidade máxima para um valor inferior à quantidade de usuários")
    void testUpdateEventInvalidCapacity() throws Exception {
        EventPutDto eventPutDto = new EventPutDto(
                "Novo Nome",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                new LocalDto("Nova Rua", "Novo Bairro", "123", "Nova Cidade", "Novo Estado", 40, 70),
                PrivacyEnum.PUBLIC,
                "Nova Descrição",
                0
        );

        driver.perform(put(EventRoutes.PUT_EVENT, eventId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventPutDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Tenta atualizar um evento sem permissão")
    void testUpdateEventForbidden() throws Exception {
        String unauthorizedToken = userSeeder.loginSegundoUser();

        EventPutDto eventPutDto = new EventPutDto(
                "Novo Nome",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                new LocalDto("Nova Rua", "Novo Bairro", "123", "Nova Cidade", "Novo Estado", 40, 70),
                PrivacyEnum.PUBLIC,
                "Nova Descrição",
                10
        );

        driver.perform(put(EventRoutes.PUT_EVENT, eventId)
                        .header("Authorization", "Bearer " + unauthorizedToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventPutDto)))
                .andExpect(status().isForbidden());
    }
    @Test
    @DisplayName("Tenta atualizar um evento com data de início posterior à data de término")
    void testUpdateEventInvalidDate() throws Exception {
        EventPutDto eventPutDto = new EventPutDto(
                "Novo Nome",
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(1),
                new LocalDto("Nova Rua", "Novo Bairro", "123", "Nova Cidade", "Novo Estado",40, 70),
                PrivacyEnum.PUBLIC,
                "Nova Descrição",
                10
        );

        driver.perform(put(EventRoutes.PUT_EVENT, eventId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventPutDto)))
                .andExpect(status().isBadRequest());
    }
}
