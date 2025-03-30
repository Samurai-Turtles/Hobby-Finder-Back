package com.hobbyFinder.hubby.controllerTest.EventTests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.controller.routes.EventRoutes;
import com.hobbyFinder.hubby.controllerTest.UserTests.UserSeeder;
import com.hobbyFinder.hubby.models.dto.events.EventDto;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.repositories.UserRepository;
import com.hobbyFinder.hubby.services.ServicesImpl.EventService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;



import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes de rota: obter evento por identificador de usuário")
public class GetEventByUserId {
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
    @Autowired
    private UserRepository userRepository;

    private String token;
    private UUID userId;
    private String token2;
    private UUID userId2;
    @Autowired
    private EventService eventService;

    @BeforeEach
    void setUp() throws Exception {
        userSeeder = new UserSeeder(driver, objectMapper);
        userSeeder.seedUsers();

        eventSeeder = new EventSeeder(driver, objectMapper, userSeeder);
        eventSeeder.seedEvents();
        this.token = eventSeeder.token;
        this.userId = userRepository.findAll().get(0).getId();
        this.token2 = userSeeder.loginSegundoUser();
        this.userId2 = userRepository.findAll().get(1).getId();
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    @DisplayName("Deve retornar 200 quando o usuário existe e tem eventos")
    void getEventByUserIdValido() throws Exception {
        String response = driver.perform(get(EventRoutes.GET_EVENT_BY_USER_ID, userId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CustomPage<EventDto> customPage = objectMapper.readValue(response, new TypeReference<CustomPage<EventDto>>() {});
        Page<EventDto> page = new PageImpl<>(customPage.getContent(), PageRequest.of(customPage.getNumber(), customPage.getSize()), customPage.getTotalElements());
        Assertions.assertFalse(page.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar 200 quando o usuário não tem eventos")
    void getEventByUserIdSemEventos() throws Exception {
        String response = driver.perform(get(EventRoutes.GET_EVENT_BY_USER_ID, userId2)
                        .header("Authorization", "Bearer " + token2))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JavaType type = objectMapper.getTypeFactory().constructParametricType(Page.class, EventDto.class);

        CustomPage<EventDto> customPage = objectMapper.readValue(response, new TypeReference<CustomPage<EventDto>>() {});
        Page<EventDto> page = new PageImpl<>(customPage.getContent(), PageRequest.of(customPage.getNumber(), customPage.getSize()), customPage.getTotalElements());
        Assertions.assertTrue(page.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar 200 quando é passado um nome")
    void getEventByUserIdComNome() throws Exception {
        String response = driver.perform(get(EventRoutes.GET_EVENT_BY_USER_ID, userId)
                        .header("Authorization", "Bearer " + token)
                        .param("name", "Fut"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CustomPage<EventDto> customPage = objectMapper.readValue(response, new TypeReference<CustomPage<EventDto>>() {});
        Page<EventDto> pages = new PageImpl<>(customPage.getContent(), PageRequest.of(customPage.getNumber(), customPage.getSize()), customPage.getTotalElements());
        Assertions.assertFalse(pages.isEmpty());
        Assertions.assertEquals(1, pages.getTotalElements());
    }
    @Test
    @DisplayName("Teste quando coloca numero de Eventos na página")
    void getEventByUserIdUmEventPerPage() throws Exception {
        String response = driver.perform(get(EventRoutes.GET_EVENT_BY_USER_ID, userId)
                        .header("Authorization", "Bearer " + token)
                        .param("eventPerPage", "1"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CustomPage<EventDto> customPage = objectMapper.readValue(response, new TypeReference<CustomPage<EventDto>>() {});
        Page<EventDto> pages = new PageImpl<>(customPage.getContent(), PageRequest.of(customPage.getNumber(), customPage.getSize()), customPage.getTotalElements());
        Assertions.assertFalse(pages.isEmpty());
    }
    @Test
    @DisplayName("Deve retornar 404 quando a pagina não existe")
    void getEventByUserIdPaginaNaoExiste() throws Exception {
        driver.perform(get(EventRoutes.GET_EVENT_BY_USER_ID, userId)
                        .header("Authorization", "Bearer " + token)
                        .param("page", "-1"))
                .andExpect(status().isNotFound());
    }
    @Test
    @DisplayName("Deve retornar 404 quando a numero de eventos na pagina é invalido")
    void getEventByUserIdEventPerPageInvalid() throws Exception {
        driver.perform(get(EventRoutes.GET_EVENT_BY_USER_ID, userId)
                        .header("Authorization", "Bearer " + token)
                        .param("eventPerPage", "-1"))
                .andExpect(status().isNotFound());
    }
}
