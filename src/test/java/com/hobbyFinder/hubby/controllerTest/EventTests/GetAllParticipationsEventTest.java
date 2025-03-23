package com.hobbyFinder.hubby.controllerTest.EventTests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.controller.routes.EventRoutes;
import com.hobbyFinder.hubby.controller.routes.UserRoutes;
import com.hobbyFinder.hubby.controllerTest.UserTests.UserSeeder;
import com.hobbyFinder.hubby.exception.CustomErrorType;
import com.hobbyFinder.hubby.exception.EventException.EventExceptionsMessages;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.ParticipationExceptionsMessages;
import com.hobbyFinder.hubby.models.dto.participationRequest.ParticipationRequestEventDto;
import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.models.enums.PrivacyEnum;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.repositories.ParticipationRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Pega todas as participações de evento")
public class GetAllParticipationsEventTest {

    @Autowired
    private MockMvc driver;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserSeeder userSeeder;
    @Autowired
    private EventSeeder eventSeeder;
    @Autowired
    private ParticipationRepository participationRepository;
    @Autowired
    private EventRepository eventRepository;
    private String token;

    @BeforeEach
    void setUp() throws Exception {
        userSeeder = new UserSeeder(driver, objectMapper);
        userSeeder.seedUsers();

        eventSeeder = new EventSeeder(driver, objectMapper, userSeeder);
        eventSeeder.seedEvents();
        this.token = eventSeeder.token;
    }

    @AfterEach
    void tearDown() throws Exception {
        this.participationRepository.deleteAll();
        this.eventRepository.deleteAll();
    }

    private UUID getEventId() {
        return eventRepository.findAll().get(0).getId();
    }

    @Transactional
    @Test
    @DisplayName("Pega todas as participações de evento com sucesso.")
    void getAllParticipationsEventTest() throws Exception {

        UUID idEvent = getEventId();

        String responseJsonString = driver.perform(get(EventRoutes.EVENT_BASE + "/" +idEvent + "/participations")
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

    @Transactional
    @Test
    @DisplayName("Identificador do evento não existe.")
    void getAllParticipationsEventNotFoundTest() throws Exception {

        UUID idEvent = UUID.randomUUID();

        String responseJsonString = driver.perform(get(EventRoutes.EVENT_BASE + "/" +idEvent + "/participations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("size", "20")
                        .param("page", "0")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType customErrorType = objectMapper.readValue(responseJsonString, CustomErrorType.class);
        assertEquals(customErrorType.getMessage(), "Evento não encontrado.");
    }

    @Transactional
    @Test
    @DisplayName("Usuário não está presente no evento.")
    void getAllParticipationsEventUserNotInParticipation() throws Exception {

        UUID idEvent = getEventId();

        driver.perform(post(UserRoutes.LOGOUT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        String secondToken = userSeeder.loginSegundoUser();

        Event event = eventRepository.findById(idEvent).get();
        event.setPrivacy(PrivacyEnum.PRIVATE);
        eventRepository.save(event);

        String responseJsonString = driver.perform(get(EventRoutes.EVENT_BASE + "/" +idEvent + "/participations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("size", "20")
                        .param("page", "0")
                        .header("Authorization", "Bearer " + secondToken))
                .andExpect(status().isForbidden())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType customErrorType = objectMapper.readValue(responseJsonString, CustomErrorType.class);
        assertEquals(customErrorType.getMessage(), ParticipationExceptionsMessages.USER_REQUISITION_DENIED);
    }

}
