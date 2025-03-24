package com.hobbyFinder.hubby.controllerTest.RequestTests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.controllerTest.UserTests.UserSeeder;
import com.hobbyFinder.hubby.exception.CustomErrorType;
import com.hobbyFinder.hubby.exception.EventException.EventExceptionsMessages;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.ParticipationExceptionsMessages;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.repositories.RequestRepository;

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
    private RequestRepository requestRepository;

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
        String uri = RequestConstants.URI_EVENT_CONTEXT.replace("{targetEventId}",
                String.valueOf(eventRepository.findAll().get(0).getId()));
        Long preExecution = requestRepository.count();

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
        String uri = RequestConstants.URI_EVENT_CONTEXT.replace("{targetEventId}", String.valueOf(9999L));
        Long preExecution = requestRepository.count();

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
        String uri = RequestConstants.URI_EVENT_CONTEXT.replace("{targetEventId}",
                String.valueOf(eventRepository.findAll().get(1).getId()));
        Long preExecution = requestRepository.count();

        String responseJsonString = driver.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isConflict())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType customErrorType = objectMapper.readValue(responseJsonString, CustomErrorType.class);

        assertAll(
                () -> assertEquals(preExecution, requestRepository.findAll().size()),
                () -> assertEquals(customErrorType.getMessage(), EventExceptionsMessages.EVENT_CROWDED));
    }

    @Test
    @Transactional
    @DisplayName("Solicitação para participar de um evento ao que o usuário já participa")
    void testUserJaParticipando() throws Exception {
        String uri = RequestConstants.URI_EVENT_CONTEXT.replace("{targetEventId}",
                String.valueOf(eventRepository.findAll().get(1).getId()));
        Long preExecution = requestRepository.count();

        String responseJsonString = driver.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenCreator))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType customErrorType = objectMapper.readValue(responseJsonString, CustomErrorType.class);

        assertAll(
                () -> assertEquals(preExecution, requestRepository.findAll().size()),
                () -> assertEquals(customErrorType.getMessage(), ParticipationExceptionsMessages.USER_ALREADY_PARTICIPATE));
    }

}
