package com.hobbyFinder.hubby.controllerTest.EventTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.controller.routes.EventRoutes;
import com.hobbyFinder.hubby.controllerTest.UserTests.UserSeeder;
import com.hobbyFinder.hubby.models.dto.events.EventCreateDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class EventSeeder {

    private final MockMvc driver;
    private final ObjectMapper objectMapper;
    private final UserSeeder userSeeder;
    protected String token;

    public EventSeeder(MockMvc driver, ObjectMapper mapper, UserSeeder userSeeder) {
        this.driver = driver;
        this.objectMapper = mapper;
        this.userSeeder = userSeeder;
    }

    public void seedEvents() throws Exception {
        this.token = userSeeder.loginPrimeiroUser();
        createEvent(EventConstants.EVENT_CREATE_DTO);
        createEvent(EventConstants.EVENT_CREATE_DTO2);
    }

    protected void createEvent(EventCreateDto eventCreateDto) throws Exception {

        driver.perform(post(EventRoutes.EVENT_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(eventCreateDto)))
                .andExpect(status().isCreated());
    }

}
