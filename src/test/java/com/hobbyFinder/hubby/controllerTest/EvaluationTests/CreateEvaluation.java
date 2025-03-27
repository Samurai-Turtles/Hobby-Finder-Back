package com.hobbyFinder.hubby.controllerTest.EvaluationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.controller.routes.EvaluationRoutes;
import com.hobbyFinder.hubby.controllerTest.EventTests.EventSeeder;
import com.hobbyFinder.hubby.controllerTest.UserTests.UserConstants;
import com.hobbyFinder.hubby.controllerTest.UserTests.UserSeeder;
import com.hobbyFinder.hubby.exception.CustomErrorType;
import com.hobbyFinder.hubby.exception.ParticipationExceptions.ParticipationExceptionsMessages;
import com.hobbyFinder.hubby.models.dto.evaluations.PostEvaluationDto;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes de rota: Avaliar Eventos em que participo.")
public class CreateEvaluation {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc driver;
    @Autowired
    EventSeeder eventSeeder;
    @Autowired
    UserSeeder userSeeder;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ParticipationRepository participationRepository;


    private String tokenSegundoUser;
    private UUID idEvent;

    private UUID getEventId() {
        return eventRepository.findAll().get(0).getId();
    }
    protected void createParticipation() {
        Participation newParticipation =
                Participation.builder().idEvent(idEvent).idUser(userRepository.findByUsername(UserConstants.USER2_USERNAME).getId())
                        .userParticipation(UserParticipation.UNCONFIRMED_PRESENCE).position(ParticipationPosition.PARTICIPANT)
                        .build();

        participationRepository.save(newParticipation);
        eventRepository.findById(idEvent).get().getParticipations().add(newParticipation);
        eventRepository.flush();
        userRepository.findByUsername(UserConstants.USER2_USERNAME).getParticipations().add(newParticipation);
        userRepository.flush();
    }

    private void endEvent() throws Exception {
        UUID idEvent = getEventId();
        eventRepository.findById(idEvent).get().setEventEnd(LocalDateTime.now());
        eventRepository.flush();
    }

    @BeforeEach
    void setUp() throws Exception {
        userSeeder.seedUsers();
        eventSeeder.seedEvents();
        this.tokenSegundoUser = userSeeder.loginSegundoUser();
        idEvent = getEventId();
        endEvent();
    }

    @Test
    @DisplayName("Criar uma avaliação válida")
    void criarAvaliacaoValida() throws Exception {
        createParticipation();

        String responseJsonString = driver.perform(post(EvaluationRoutes.POST_EVALUATION_EVENT, idEvent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenSegundoUser)
                        .content(objectMapper.writeValueAsString(EvaluationConstants.EVALUATION_CREATE_DTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        ResponseEvaluationDto result = objectMapper.readValue(responseJsonString, ResponseEvaluationDto.class);

        Event event = eventRepository.findById(idEvent).get();
        User creator = event.getCreator();

        assertAll(
                () -> assertEquals(EvaluationConstants.FIVE_STARS, result.star()),
                () -> assertEquals(EvaluationConstants.FIVE_STARS, event.getAvaliationStars()),
                () -> assertEquals(EvaluationConstants.FIVE_STARS, creator.getStars())
        );

    }

    @Test
    @DisplayName("Criar uma avaliação com estrelas menor que permitido")
    void criarAvaliacaoEstrelasMenorQuePermitido() throws Exception {
        createParticipation();


        PostEvaluationDto EVALUATION_INVALID_STARS = new PostEvaluationDto(
                EvaluationConstants.INVALID_STARS_LESS,
                EvaluationConstants.COMMENT);

        String responseJsonString = driver.perform(post(EvaluationRoutes.POST_EVALUATION_EVENT, idEvent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenSegundoUser)
                        .content(objectMapper.writeValueAsString(EVALUATION_INVALID_STARS)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType error = objectMapper.readValue(responseJsonString, CustomErrorType.class);

        assertEquals("Avaliação deve ser no mínimo 0", error.getMessage());
    }

    @Test
    @DisplayName("Criar uma avaliação com estrelas maior que permitido")
    void criarAvaliacaoEstrelasMaiorQuePermitido() throws Exception {
        createParticipation();

        PostEvaluationDto EVALUATION_INVALID_STARS = new PostEvaluationDto(
                EvaluationConstants.INVALID_STARS_PLUS,
                EvaluationConstants.COMMENT);

        String responseJsonString = driver.perform(post(EvaluationRoutes.POST_EVALUATION_EVENT, idEvent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenSegundoUser)
                        .content(objectMapper.writeValueAsString(EVALUATION_INVALID_STARS)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType error = objectMapper.readValue(responseJsonString, CustomErrorType.class);

        assertEquals("Avaliação deve ser no máximo 5", error.getMessage());
    }

    @Test
    @DisplayName("Criar uma avaliação em um evento inexistente")
    void criarAvaliacaoEventoNaoExiste() throws Exception {
        UUID randomIdEvent = UUID.randomUUID();

        String responseJsonString = driver.perform(post(EvaluationRoutes.POST_EVALUATION_EVENT, randomIdEvent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenSegundoUser)
                        .content(objectMapper.writeValueAsString(EvaluationConstants.EVALUATION_CREATE_DTO)))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType result = objectMapper.readValue(responseJsonString, CustomErrorType.class);

        assertEquals("Evento não encontrado.", result.getMessage());

    }

    @Test
    @DisplayName("Criar uma avaliação com o usuário sem participar do evento")
    void criarAvaliacaoNaoParticipanteEvento() throws Exception {
        String responseJsonString = driver.perform(post(EvaluationRoutes.POST_EVALUATION_EVENT, idEvent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenSegundoUser)
                        .content(objectMapper.writeValueAsString(EvaluationConstants.EVALUATION_CREATE_DTO)))
                .andExpect(status().isForbidden())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType result = objectMapper.readValue(responseJsonString, CustomErrorType.class);

        assertEquals(ParticipationExceptionsMessages.USER_REQUISITION_DENIED, result.getMessage());
    }
}