package com.hobbyFinder.hubby.controllerTest.PartiticipationRequestTests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.controller.routes.EventRoutes;
import com.hobbyFinder.hubby.controllerTest.EventTests.EventSeeder;
import com.hobbyFinder.hubby.controllerTest.UserTests.UserSeeder;
import com.hobbyFinder.hubby.models.dto.events.EventCreateDto;
import com.hobbyFinder.hubby.repositories.EventRepository;

@Component
public class RequestSeeder {

    private final MockMvc driver;
    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper;
    private final UserSeeder userSeeder;
    private final EventSeeder eventSeeder;
    protected String token;

    public RequestSeeder(MockMvc driver, ObjectMapper mapper, UserSeeder userSeeder, EventRepository eventRepository) {
        this.driver = driver;
        this.objectMapper = mapper;
        this.userSeeder = userSeeder;
        this.eventRepository = eventRepository;
        this.eventSeeder = new EventSeeder(driver, mapper, userSeeder);

    }

    public void seedRequest(UUID eventId) throws Exception {
        this.token = userSeeder.loginPrimeiroUser();

        createRequest(eventId);
    }

    protected void createRequest(UUID eventId) throws Exception {
        String uri = "/api/evento/" + eventId + "/request";

        driver.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    protected void createEvent(EventCreateDto eventCreateDto) throws Exception {

        driver.perform(post(EventRoutes.EVENT_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(eventCreateDto)))
                .andExpect(status().isCreated());
    }

    protected void createEvent(EventCreateDto eventCreateDto, String tokenCreator) throws Exception {

        driver.perform(post(EventRoutes.EVENT_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenCreator)
                .content(objectMapper.writeValueAsString(eventCreateDto)))
                .andExpect(status().isCreated());
    }

}
