package com.hobbyFinder.hubby.controllerTest.PartiticipationRequestTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.controllerTest.UserTests.UserSeeder;
import com.hobbyFinder.hubby.exception.CustomErrorType;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.ParticipationExceptionsMessages;
import com.hobbyFinder.hubby.models.dto.participationRequest.ParticipationRequestEventDto;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.repositories.ParticipationRequestRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Testes da rota: obter solicitações por usuário")
public class GetAllUserRequestTest {
    
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
        requestSeeder.createEvent(RequestConstants.EVENT_PUBLIC, tokenCreator);
    }

    @AfterEach
    void tearDown() {
        eventRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("Listar solicitações de participação de um usuario")
    void testListaSolicitacoesUsuario() throws Exception {
        UUID eventId = eventRepository.findAll().get(0).getId();
        requestSeeder.seedRequest(eventId, token);

        String responseJsonString = driver.perform(get(RequestConstants.URI_USER_CONTEXT)
                .contentType(MediaType.APPLICATION_JSON)
                .param("size", "20")
                .param("page", "0")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode rootNode = objectMapper.readTree(responseJsonString);
        JsonNode contentNode = rootNode.get("content");
        List<ParticipationRequestEventDto> content = objectMapper.readValue(
                contentNode.toString(),
                new TypeReference<List<ParticipationRequestEventDto>>() {
                });

        assertEquals(1, content.size());
    }

    @Test
    @Transactional
    @DisplayName("Tentar listar solicitações de participação de um Usuario sem solicitações")
    void testListaSemSolicitacoesUsuario() throws Exception {

        String responseJsonString = driver.perform(get(RequestConstants.URI_USER_CONTEXT)
                .contentType(MediaType.APPLICATION_JSON)
                .param("size", "20")
                .param("page", "0")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType customErrorType = objectMapper.readValue(responseJsonString, CustomErrorType.class);

        assertEquals(customErrorType.getMessage(), "A página indicada está vazia");
    }

}
