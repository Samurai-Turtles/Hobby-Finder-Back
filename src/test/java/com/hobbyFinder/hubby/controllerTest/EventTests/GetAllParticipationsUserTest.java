package com.hobbyFinder.hubby.controllerTest.EventTests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.controller.routes.UserRoutes;
import com.hobbyFinder.hubby.controllerTest.PartiticipationRequestTests.RequestConstants;
import com.hobbyFinder.hubby.controllerTest.UserTests.UserConstants;
import com.hobbyFinder.hubby.controllerTest.UserTests.UserSeeder;
import com.hobbyFinder.hubby.exception.CustomErrorType;
import com.hobbyFinder.hubby.models.dto.participationRequest.ParticipationRequestEventDto;
import com.hobbyFinder.hubby.models.entities.Participation;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.repositories.ParticipationRepository;
import com.hobbyFinder.hubby.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Pega todas as participações de evento")

public class GetAllParticipationsUserTest {

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

    @Transactional
    @Test
    @DisplayName("Pega todas as participações de usuário")
    void getAllParticipationsUser() throws Exception {

        String responseJsonString = driver.perform(get(UserRoutes.GET_ALL_USER_PARTICIPATIONS)
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

        System.out.println("Lista convertida: " + content);

        assertEquals(2, content.size());

    }

    @Transactional
    @Test
    @DisplayName("Quando não existe participações do usuário.")
    void getAllParticipationsFail() throws Exception {

        driver.perform(post(UserRoutes.LOGOUT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        String secondToken = userSeeder.loginSegundoUser();

        String responseJsonString = driver.perform(get(UserRoutes.GET_ALL_USER_PARTICIPATIONS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("size", "20")
                        .param("page", "0")
                        .header("Authorization", "Bearer " + secondToken))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType customErrorType = objectMapper.readValue(responseJsonString, CustomErrorType.class);
        assertEquals(customErrorType.getMessage(), "A página indicada está vazia");
    }
}
