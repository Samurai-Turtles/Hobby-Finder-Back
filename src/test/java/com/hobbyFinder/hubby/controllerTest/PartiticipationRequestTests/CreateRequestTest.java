package com.hobbyFinder.hubby.controllerTest.PartiticipationRequestTests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.beans.Transient;

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
import org.w3c.dom.events.EventException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.controller.routes.EventRoutes;
import com.hobbyFinder.hubby.controllerTest.UserTests.UserSeeder;
import com.hobbyFinder.hubby.exception.CustomErrorType;
import com.hobbyFinder.hubby.exception.EventException.EventExceptionsMessages;
import com.hobbyFinder.hubby.models.dto.events.EventCreateDto;
import com.hobbyFinder.hubby.models.entities.Participation;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.repositories.ParticipationRequestRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Testes da rota: criar solicitação")
public class CreateRequestTest {

    private String token;

    private String tokenCreator;

    @Autowired
    private MockMvc driver;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RequestSeeder requestSeeder;

    @Autowired
    private UserSeeder userSeeder;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ParticipationRequestRepository requestRepository;

    @BeforeEach
    void setUp() throws Exception {
        requestSeeder = new RequestSeeder(driver, objectMapper, userSeeder, eventRepository);
        userSeeder.seedUsers();
        token = userSeeder.loginSegundoUser();

        tokenCreator = userSeeder.loginPrimeiroUser();
        requestSeeder.createEvent(RequestConstants.EVENT_PRIVATE_MAX_5, tokenCreator);
        requestSeeder.createEvent(RequestConstants.EVENT_PRIVATE_MAX_1, tokenCreator);
    }

    @AfterEach
    void tearDown() {
        eventRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("Solicitação registrada com sucesso")
    void testNovaSolicitacao() throws Exception {
        Long preExecution = requestRepository.count();
        String uri = EventRoutes.EVENT_BASE + "/" + eventRepository.findAll().get(0).getId() + "/request";

        driver.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        assertNotEquals(preExecution, requestRepository.findAll().size());
    }

    @Test
    @Transactional
    @DisplayName("Solicitação com id de evento inválido")
    void testIdEventoInvalido() throws Exception {
        Long preExecution = requestRepository.count();
        String uri = EventRoutes.EVENT_BASE + "/" + 9999L + "/request";

        driver.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());

        assertEquals(preExecution, requestRepository.findAll().size());

    }

    @Test
    @Transactional
    @DisplayName("Solicitação para participar de um evento cheio")
    void testIdEventoCheio() throws Exception {
        Long preExecution = requestRepository.count();
        String uri = EventRoutes.EVENT_BASE + "/" + eventRepository.findAll().get(1).getId() + "/request";

        driver.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isConflict())
                .andReturn().getResponse().getContentAsString();

        assertEquals(preExecution, requestRepository.findAll().size());
    }

    @Test
    @Transactional
    @DisplayName("Solicitação para participar de um evento ao que o usuário já participa")
    void testUserJaParticipando() throws Exception {
        Long preExecution = requestRepository.count();
        String uri = EventRoutes.EVENT_BASE + "/" + eventRepository.findAll().get(1).getId() + "/request";

        driver.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenCreator))
                .andExpect(status().isUnprocessableEntity());

        assertEquals(preExecution, requestRepository.findAll().size());
    }

}
