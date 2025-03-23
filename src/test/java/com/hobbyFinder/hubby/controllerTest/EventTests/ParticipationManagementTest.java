package com.hobbyFinder.hubby.controllerTest.EventTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.controller.routes.EventRoutes;
import com.hobbyFinder.hubby.controller.routes.UserRoutes;
import com.hobbyFinder.hubby.controllerTest.UserTests.UserConstants;
import com.hobbyFinder.hubby.controllerTest.UserTests.UserSeeder;
import com.hobbyFinder.hubby.exception.CustomErrorType;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.ParticipationExceptionsMessages;
import com.hobbyFinder.hubby.models.dto.participations.UpdateParticipationDto;
import com.hobbyFinder.hubby.models.entities.Participation;
import com.hobbyFinder.hubby.models.enums.ParticipationPosition;
import com.hobbyFinder.hubby.models.enums.UserParticipation;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Gerenciamento de participação.")
public class ParticipationManagementTest {

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
    @Autowired
    private UserRepository userRepository;
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

    private UUID createParticipation() throws Exception {

        UUID idEvent = getEventId();
        Participation newParticipation =
                Participation.builder().idEvent(idEvent).idUser(userRepository.findByUsername(UserConstants.USER2_USERNAME).getId())
                        .userParticipation(UserParticipation.UNCONFIRMED_PRESENCE).position(ParticipationPosition.PARTICIPANT)
                        .build();

        participationRepository.save(newParticipation);
        eventRepository.findById(idEvent).get().getParticipations().add(newParticipation);
        eventRepository.flush();
        userRepository.findByUsername(UserConstants.USER2_USERNAME).getParticipations().add(newParticipation);
        userRepository.flush();

        return eventRepository.findById(idEvent).get().getParticipations().get(1).getIdParticipation();
    }

    @Transactional
    @Test
    @DisplayName("Gerenciamento de participação bem sucedido")
    void participationManagementSucess() throws Exception {

        UUID idEvent = getEventId();
        UUID newParticipation = createParticipation();
        UpdateParticipationDto updateParticipationDto = new UpdateParticipationDto(UserParticipation.CONFIRMED_PRESENCE, ParticipationPosition.ADMIN);

        driver.perform(put(EventRoutes.EVENT_BASE + "/" + idEvent + "/participation/" + newParticipation)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(updateParticipationDto)))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    @DisplayName("Identificador de evento não encontrado.")
    void participationManagementFailEvent() throws Exception {

        UUID idEvent = UUID.randomUUID();
        UUID newParticipation = createParticipation();
        UpdateParticipationDto updateParticipationDto = new UpdateParticipationDto(UserParticipation.CONFIRMED_PRESENCE, ParticipationPosition.ADMIN);

        String responseJsonString = driver.perform(put(EventRoutes.EVENT_BASE + "/" + idEvent + "/participation/" + newParticipation)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(updateParticipationDto)))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType customErrorType = objectMapper.readValue(responseJsonString, CustomErrorType.class);
        assertEquals(customErrorType.getMessage(),"Evento não encontrado.");
    }

    @Transactional
    @Test
    @DisplayName("Identificador de participação não encontrado.")
    void participationManagementFailParticipation() throws Exception {

        UUID idEvent = getEventId();
        UUID newParticipation = UUID.randomUUID();
        UpdateParticipationDto updateParticipationDto = new UpdateParticipationDto(UserParticipation.CONFIRMED_PRESENCE, ParticipationPosition.ADMIN);

        String responseJsonString = driver.perform(put(EventRoutes.EVENT_BASE + "/" + idEvent + "/participation/" + newParticipation)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(updateParticipationDto)))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType customErrorType = objectMapper.readValue(responseJsonString, CustomErrorType.class);
        assertEquals(customErrorType.getMessage(),"Participação não encontrada!");
    }

    @Transactional
    @Test
    @DisplayName("Cargo do participante a ser atualizado é inferior ou igual ao usuário logado")
    void participationManagementIncorrectPosition() throws Exception {

        UUID idEvent = getEventId();
        UUID idParticipation = eventRepository.findById(idEvent).get().getParticipations().get(0).getIdParticipation();
        createParticipation();
        UpdateParticipationDto updateParticipationDto = new UpdateParticipationDto(UserParticipation.CONFIRMED_PRESENCE, ParticipationPosition.ADMIN);

        driver.perform(post(UserRoutes.LOGOUT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        //Logando o outro usuário.
        String segundoToken = userSeeder.loginSegundoUser();

        String responseJsonString = driver.perform(put(EventRoutes.EVENT_BASE + "/" + idEvent + "/participation/" + idParticipation)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + segundoToken)
                        .content(objectMapper.writeValueAsString(updateParticipationDto)))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType customErrorType = objectMapper.readValue(responseJsonString, CustomErrorType.class);
        assertEquals(customErrorType.getMessage(), ParticipationExceptionsMessages.USER_POSITION_DENIED);
    }

}
