package com.hobbyFinder.hubby.controllerTest.EventTests;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.controller.routes.EventRoutes;
import com.hobbyFinder.hubby.controllerTest.UserTests.UserSeeder;
import com.hobbyFinder.hubby.models.dto.events.EventCreateDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes de rota: criação de evento")
public class CreateTest {

    @Autowired
    private MockMvc driver;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserSeeder userSeeder;
    @Autowired
    private EventSeeder eventSeeder;

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
    void tearDown() throws Exception {}

    @Transactional
    @Test
    @DisplayName("Evento cadastrado com sucesso")
    void testCreateEventSucess() throws Exception {

        EventCreateDto eventCreateDto = new EventCreateDto(EventConstants.UNUSED_NAME_EVENT, EventConstants.UNUSED_DATE_TIME_EVENT_BEGIN,
                EventConstants.UNUSED_DATE_TIME_EVENT_END, EventConstants.UNUSED_LOCAL, EventConstants.UNUSED_PRIVACY_ENUM,
                EventConstants.UNUSED_DESCRIPTION, EventConstants.UNUSED_MAX_USER_AMOUNT);

        driver.perform(post(EventRoutes.POST_EVENT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(eventCreateDto)))
                .andExpect(status().isCreated());

    }
}
