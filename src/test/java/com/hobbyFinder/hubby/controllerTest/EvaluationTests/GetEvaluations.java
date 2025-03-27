package com.hobbyFinder.hubby.controllerTest.EvaluationTests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.controller.routes.EvaluationRoutes;
import com.hobbyFinder.hubby.controllerTest.UserTests.UserConstants;
import com.hobbyFinder.hubby.controllerTest.UserTests.UserSeeder;
import com.hobbyFinder.hubby.exception.CustomErrorType;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.ParticipationExceptionsMessages;
import com.hobbyFinder.hubby.models.dto.evaluations.ResponseEvaluationDto;
import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.models.entities.Participation;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.models.enums.ParticipationPosition;
import com.hobbyFinder.hubby.models.enums.UserParticipation;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.repositories.ParticipationRepository;
import com.hobbyFinder.hubby.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes de rota: obter avaliações de eventos em que participo.")
public class GetEvaluations {

    @Autowired
    MockMvc driver;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserSeeder userSeeder;
    @Autowired
    EvaluationSeeder evaluationSeeder;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ParticipationRepository participationRepository;

    private String tokenFirstUser;
    private String tokenSecondUser;

    private UUID idFirstEvent;
    private UUID idSecondEvent;

    @BeforeEach
    void setUp() throws Exception {
        evaluationSeeder.seedEvaluations();
        this.idFirstEvent = evaluationSeeder.getFirstEventId();
        this.idSecondEvent = evaluationSeeder.getSecondEventId();
        this.tokenFirstUser = userSeeder.loginPrimeiroUser();
        this.tokenSecondUser = userSeeder.loginSegundoUser();
    }

    @Test
    @DisplayName("Recuperar avaliações do primeiro evento")
    void getAvaliacaoPrimeiroEvento() throws Exception {
        String responseJsonString = driver.perform(get(EvaluationRoutes.GET_EVALUATION_EVENT, this.idFirstEvent)
                .header("Authorization", "Bearer " + this.tokenFirstUser))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();


        Collection<ResponseEvaluationDto> responseList = objectMapper.readValue(responseJsonString,
                new TypeReference<>() {});

        Event event = eventRepository.findById(this.idFirstEvent).get();
        User creator = event.getCreator();

        assertAll(
                () -> assertNotNull(responseList),
                () -> assertEquals(5, responseList.size()),
                () -> assertEquals(2, event.getAvaliationStars()),
                () -> assertEquals(2.5, creator.getStars())
        );
    }

    @Test
    @DisplayName("Recuperar avaliações do segundo evento")
    void getAvaliacaoSegundoEvento() throws Exception {
        String responseJsonString = driver.perform(get(EvaluationRoutes.GET_EVALUATION_EVENT, this.idSecondEvent)
                        .header("Authorization", "Bearer " + this.tokenFirstUser))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Collection<ResponseEvaluationDto> responseList = objectMapper.readValue(responseJsonString,
                new TypeReference<>() {});

        Event event = eventRepository.findById(this.idSecondEvent).get();
        User creator = event.getCreator();

        assertAll(
                () -> assertNotNull(responseList),
                () -> assertEquals(5, responseList.size()),
                () -> assertEquals(3, event.getAvaliationStars()),
                () -> assertEquals(2.5, creator.getStars())
        );
    }

    @Test
    @DisplayName("Recuperar avaliações de um evento inexistente")
    void getAvaliacaoEventoInexistente() throws Exception {
        UUID idRandomEvent = UUID.randomUUID();

        String responseJsonString = driver.perform(get(EvaluationRoutes.GET_EVALUATION_EVENT, idRandomEvent)
                        .header("Authorization", "Bearer " + this.tokenFirstUser))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType result = objectMapper.readValue(responseJsonString, CustomErrorType.class);

        assertEquals("Evento não encontrado.", result.getMessage());
    }

    @Test
    @DisplayName("Recuperar avaliações de um evento que não participo")
    void getAvaliacaoEventoQueNaoParticipo() throws Exception {

        String responseJsonString = driver.perform(get(EvaluationRoutes.GET_EVALUATION_EVENT, this.idFirstEvent)
                        .header("Authorization", "Bearer " + this.tokenSecondUser))
                .andExpect(status().isForbidden())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType result = objectMapper.readValue(responseJsonString, CustomErrorType.class);

        assertEquals(ParticipationExceptionsMessages.USER_REQUISITION_DENIED, result.getMessage());
    }


    @Test
    @DisplayName("Recuperar avaliações de um evento que não criei")
    void getAvaliacaoEventoQueNaoCriei() throws Exception {
        createParticipation();

        String responseJsonString = driver.perform(get(EvaluationRoutes.GET_EVALUATION_EVENT, this.idFirstEvent)
                        .header("Authorization", "Bearer " + this.tokenSecondUser))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType result = objectMapper.readValue(responseJsonString, CustomErrorType.class);

        assertEquals(ParticipationExceptionsMessages.USER_POSITION_DENIED, result.getMessage());
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

    private UUID getEventId() {
        return eventRepository.findAll().get(0).getId();
    }
}