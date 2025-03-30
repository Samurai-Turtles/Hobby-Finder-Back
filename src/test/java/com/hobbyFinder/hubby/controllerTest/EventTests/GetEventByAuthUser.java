package com.hobbyFinder.hubby.controllerTest.EventTests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.controller.routes.EventRoutes;
import com.hobbyFinder.hubby.controllerTest.UserTests.UserSeeder;
import com.hobbyFinder.hubby.models.dto.events.EventDto;
import com.hobbyFinder.hubby.repositories.EventRepository;
import com.hobbyFinder.hubby.repositories.UserRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes de rota: obter eventos por proximidade geográfica")
public class GetEventByAuthUser {
    @Autowired
    private MockMvc driver;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserSeeder userSeeder;
    @Autowired
    private EventSeeder eventSeeder;

    private String authToken;

    @BeforeEach
    void setUp() throws Exception {
        userSeeder.seedUsers();
        eventSeeder.seedEvents();
        this.authToken = eventSeeder.token;
    }

    @Test
    @DisplayName("Deve retornar 200 com eventos próximos quando fornecido latitude e longitude")
    void deveRetornarEventosProximosQuandoLocalizacaoFornecida() throws Exception {
        String response = driver.perform(get(EventRoutes.GET_EVENT_BY_AUTH_USER)
                        .header("Authorization", "Bearer " + authToken)
                        .param("latitude", "-23.5505")
                        .param("longitude", "-46.6333"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CustomPage<EventDto> customPage = objectMapper.readValue(response, new TypeReference<CustomPage<EventDto>>() {});
        Page<EventDto> page = new PageImpl<>(customPage.getContent(), PageRequest.of(customPage.getNumber(), customPage.getSize()), customPage.getTotalElements());
        assertFalse(page.isEmpty(), "Deveria retornar eventos próximos");
    }

    @Test
    @DisplayName("Deve filtrar eventos quando nome é fornecido")
    void deveFiltrarEventosPorNomeQuandoFornecido() throws Exception {
        String response = driver.perform(get(EventRoutes.GET_EVENT_BY_AUTH_USER)
                        .header("Authorization", "Bearer " + authToken)
                        .param("name", "Fut"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CustomPage<EventDto> customPage = objectMapper.readValue(response, new TypeReference<CustomPage<EventDto>>() {});
        Page<EventDto> page = new PageImpl<>(customPage.getContent(), PageRequest.of(customPage.getNumber(), customPage.getSize()), customPage.getTotalElements());
        assertFalse(page.isEmpty(), "Deveria retornar eventos filtrados por nome");
    }

    @Test
    @DisplayName("Deve retornar 404 quando página não existe")
    void deveRetornarNotFoundQuandoPaginaNaoExiste() throws Exception {
        driver.perform(get(EventRoutes.GET_EVENT_BY_AUTH_USER)
                        .header("Authorization", "Bearer " + authToken)
                        .param("latitude", "-23.5505")
                        .param("longitude", "-46.6333")
                        .param("page", "-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 403 quando usuário não autenticado")
    void deveRetornarForbiddenQuandoNaoAutenticado() throws Exception {
        driver.perform(get(EventRoutes.GET_EVENT_BY_AUTH_USER)
                        .param("latitude", "-23.5505")
                        .param("longitude", "-46.6333"))
                .andExpect(status().isForbidden());
    }
}