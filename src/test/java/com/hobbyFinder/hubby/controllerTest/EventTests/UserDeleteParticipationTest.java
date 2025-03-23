package com.hobbyFinder.hubby.controllerTest.EventTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.controller.routes.EventRoutes;
import com.hobbyFinder.hubby.controller.routes.UserRoutes;
import com.hobbyFinder.hubby.controllerTest.UserTests.UserSeeder;
import com.hobbyFinder.hubby.exception.CustomErrorType;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.ParticipationExceptionsMessages;
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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes de rota: criação de evento")
public class UserDeleteParticipationTest {

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

    //Função apenas utilizada enquanto a rota de getEvent não está pronto
    private UUID getEventId() {
        return eventRepository.findAll().get(0).getId();
    }

    @Transactional
    @Test
    @DisplayName("User deleta sua participação com sucesso.")
    void testSelfUserDeleteParticipation() throws Exception {

        UUID idEvent = getEventId();
        UUID idParticipation = eventRepository.findById(idEvent).get().getParticipations().get(0).getIdParticipation();

        driver.perform(delete(UserRoutes.USER_BASE + "/delete-event/" + idEvent + "/participation/" + idParticipation)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

    }

    @Transactional
    @Test
    @DisplayName("Identificador do evento é o mesmo do parâmetro passado.")
    void testSelfUserDeleteParticipationNotFound() throws Exception {

        UUID idEvent = getEventId();
        UUID idParticipation = eventRepository.findById(idEvent).get().getParticipations().get(0).getIdParticipation();
        UUID idRandomEvent = UUID.randomUUID();

        String responseJsonString = driver.perform(delete(UserRoutes.USER_BASE + "/delete-event/" + idRandomEvent + "/participation/" + idParticipation)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isConflict())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType customErrorType = objectMapper.readValue(responseJsonString, CustomErrorType.class);
        assertEquals(customErrorType.getMessage(), ParticipationExceptionsMessages.INCORRECT_EVENT_ID);

    }

    @Transactional
    @Test
    @DisplayName("Identificador de participação não foi cadastrado")
    void testSelfUserDeleteParticipationInvalid() throws Exception {
        UUID idEvent = getEventId();
        UUID idParticipation = UUID.randomUUID();

        String responseJsonString = driver.perform(delete(UserRoutes.USER_BASE + "/delete-event/" + idEvent + "/participation/" + idParticipation)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType customErrorType = objectMapper.readValue(responseJsonString, CustomErrorType.class);
        assertEquals(customErrorType.getMessage(),"Participação não encontrada!");
    }

    //Esse teste só existe por causa de uma singularidade do código, pois foi pedido para ser armazenado ids.
    @Transactional
    @Test
    @DisplayName("O usuário está tentando excluir a participação de outro user.")
    void testSelfDeleteIncorrectUser() throws Exception {

        UUID idEvent = getEventId();
        UUID idParticipation = eventRepository.findById(idEvent).get().getParticipations().get(0).getIdParticipation();

        //Fazendo logout do usuário que criou o evento
        driver.perform(post(UserRoutes.LOGOUT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        //Logando o usuário incorreto
        String incorrectToken = userSeeder.loginSegundoUser();

        String responseJsonString = driver.perform(delete(UserRoutes.USER_BASE + "/delete-event/" + idEvent + "/participation/" + idParticipation)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + incorrectToken))
                .andExpect(status().isConflict())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType customErrorType = objectMapper.readValue(responseJsonString, CustomErrorType.class);
        assertEquals(customErrorType.getMessage(),ParticipationExceptionsMessages.SELF_DELETE_ID_DENIED);

    }

}
