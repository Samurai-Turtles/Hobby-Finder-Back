package com.hobbyFinder.hubby.controllerTest.EventTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.controller.routes.EventRoutes;
import com.hobbyFinder.hubby.controllerTest.UserTests.UserConstants;
import com.hobbyFinder.hubby.controllerTest.UserTests.UserSeeder;
import com.hobbyFinder.hubby.exception.CustomErrorType;
import com.hobbyFinder.hubby.exception.EventException.EventExceptionsMessages;
import com.hobbyFinder.hubby.models.dto.events.EventCreateDto;
import com.hobbyFinder.hubby.repositories.EventRepository;
import jakarta.transaction.Transactional;
import org.checkerframework.checker.units.qual.A;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        this.eventRepository.deleteAll();
    }

    @Transactional
    @Test
    @DisplayName("Evento cadastrado com sucesso")
    void testCreateEventSucess() throws Exception {

        EventCreateDto eventCreateDto = new EventCreateDto(EventConstants.UNUSED_NAME_EVENT, EventConstants.UNUSED_DATE_TIME_EVENT_BEGIN,
                EventConstants.UNUSED_DATE_TIME_EVENT_END, EventConstants.UNUSED_LOCAL, EventConstants.UNUSED_PRIVACY_ENUM,
                EventConstants.UNUSED_DESCRIPTION, EventConstants.UNUSED_MAX_USER_AMOUNT, UserConstants.INTERESSE_USADO);

        driver.perform(post(EventRoutes.POST_EVENT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(eventCreateDto)))
                .andExpect(status().isCreated());

    }

    @Transactional
    @Test
    @DisplayName("Evento cadastrado com qualquer campo nulo que não seja max_user_amount e description.")
    void testCreateEventNull() throws Exception {

        EventCreateDto eventCreateDto = new EventCreateDto(null, EventConstants.UNUSED_DATE_TIME_EVENT_BEGIN,
                EventConstants.UNUSED_DATE_TIME_EVENT_END, EventConstants.UNUSED_LOCAL, EventConstants.UNUSED_PRIVACY_ENUM,
                EventConstants.UNUSED_DESCRIPTION, EventConstants.UNUSED_MAX_USER_AMOUNT, UserConstants.INTERESSE_USADO);

        driver.perform(post(EventRoutes.POST_EVENT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(eventCreateDto)))
                .andExpect(status().isBadRequest());

    }

    @Transactional
    @Test
    @DisplayName("Data do fim de evento não pode ser antes do que data de início.")
    void testEventIncorrectDate() throws Exception {

        EventCreateDto eventCreateDto = new EventCreateDto(EventConstants.UNUSED_NAME_EVENT, EventConstants.UNUSED_DATE_TIME_EVENT_BEGIN,
                LocalDateTime.now().minusMonths(12), EventConstants.UNUSED_LOCAL, EventConstants.UNUSED_PRIVACY_ENUM,
                EventConstants.UNUSED_DESCRIPTION, EventConstants.UNUSED_MAX_USER_AMOUNT, UserConstants.INTERESSE_USADO);

        String responseJsonString = driver.perform(post(EventRoutes.POST_EVENT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(eventCreateDto)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType customErrorType = objectMapper.readValue(responseJsonString, CustomErrorType.class);
        assertEquals(EventExceptionsMessages.INCORRECT_DATE, customErrorType.getMessage());
    }
}
