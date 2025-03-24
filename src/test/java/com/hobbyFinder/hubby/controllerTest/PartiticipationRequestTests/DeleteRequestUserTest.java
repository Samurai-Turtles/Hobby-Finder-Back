package com.hobbyFinder.hubby.controllerTest.PartiticipationRequestTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

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
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.repositories.RequestRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Testes da rota: usuário deleta sua solicitação")
public class DeleteRequestUserTest {

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
        requestSeeder.createEvent(RequestConstants.EVENT_PUBLIC, tokenCreator);
    }

    @AfterEach
    void tearDown() {
        eventRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("Rejeita a Solicitação de um usuário")
    void testDeclineSolicitacoesUsuario() throws Exception {
        UUID eventId = eventRepository.findAll().get(0).getId();
        String uri = RequestConstants.URI_USER_CONTEXT;

        requestSeeder.seedRequest(eventId, token);

        driver.perform(delete(uri + "/" + requestRepository.findAll().get(0).getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

    }

    @Test
    @Transactional
    @DisplayName("Tentar rejeitar a solicitação de participação com um id inválido")
    void testDeclineSolicitacoesEventoIdErrado() throws Exception {
        UUID eventId = eventRepository.findAll().get(0).getId();
        String uri = RequestConstants.URI_USER_CONTEXT;

        requestSeeder.seedRequest(eventId, token);

        String responseJsonString = driver.perform(delete(uri + "/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType customErrorType = objectMapper.readValue(responseJsonString, CustomErrorType.class);
        assertEquals(customErrorType.getMessage(), "Requisição não encontrada.");
    }

}
