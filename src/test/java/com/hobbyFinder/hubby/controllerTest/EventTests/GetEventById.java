package com.hobbyFinder.hubby.controllerTest.EventTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.controller.routes.EventRoutes;
import com.hobbyFinder.hubby.controllerTest.UserTests.UserSeeder;
import com.hobbyFinder.hubby.models.dto.events.EventDto;
import com.hobbyFinder.hubby.models.dto.events.EventPutDto;
import com.hobbyFinder.hubby.models.enums.PrivacyEnum;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.services.ServicesImpl.EventService;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes de rota: obter evento por identificador de Evento")
public class GetEventById {

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
    @Autowired
    private EventService eventService;

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
    void tearDown() throws Exception {
    }

    private UUID getEventId() {
        return eventRepository.findAll().get(0).getId();
    }

    @Test
    @DisplayName("Obtém um evento público com sucesso")
    void testGetEventSuccess() throws Exception {
        String responseJsonString = driver.perform(MockMvcRequestBuilders.get(EventRoutes.GET_EVENT_BY_ID, eventId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        EventDto response = objectMapper.readValue(responseJsonString, EventDto.class);

        assertNotNull(response);
        assertEquals(eventId, response.id());
        assertNotNull(response.description());
    }
    @Test
    @DisplayName("Tenta obter um evento com ID inexistente")
    void testGetEventNotFound() throws Exception {
        UUID nonExistentEventId = UUID.randomUUID();

        driver.perform(MockMvcRequestBuilders.get(EventRoutes.GET_EVENT_BY_ID, nonExistentEventId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @Test
    @DisplayName("Obtém um evento privado sem ser participante - deve retornar dados limitados")
    void testGetPrivateEventNotParticipant() throws Exception {
        String otherUserToken = userSeeder.loginSegundoUser();
        EventPutDto eventPutDto = new EventPutDto(null, null, null, null, PrivacyEnum.PRIVATE, null, null, null);
        driver.perform(put(EventRoutes.PUT_EVENT, eventId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventPutDto)));

        String responseJsonString = driver.perform(MockMvcRequestBuilders.get(EventRoutes.GET_EVENT_BY_ID, eventId)
                        .header("Authorization", "Bearer " + otherUserToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        EventDto response = objectMapper.readValue(responseJsonString, EventDto.class);

        assertNotNull(response);
        assertEquals(eventId, response.id());
        assertEquals(PrivacyEnum.PRIVATE, response.privacy());
        assertNull(response.description());
    }

    @Test
    @DisplayName("Obtém um evento privado sendo participante ")
    void testGetPrivateEventAsParticipant() throws Exception {
        EventPutDto eventPutDto = new EventPutDto(null, null, null, null, PrivacyEnum.PRIVATE, null, null, null);
        driver.perform(put(EventRoutes.PUT_EVENT, eventId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventPutDto)));

        String responseJsonString = driver.perform(MockMvcRequestBuilders.get(EventRoutes.GET_EVENT_BY_ID, eventId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        EventDto response = objectMapper.readValue(responseJsonString, EventDto.class);

        assertNotNull(response);
        assertEquals(eventId, response.id());
        assertEquals(PrivacyEnum.PRIVATE, response.privacy());
        assertNotNull(response.description());
    }
}