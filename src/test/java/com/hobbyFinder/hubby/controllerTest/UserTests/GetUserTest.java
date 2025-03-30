package com.hobbyFinder.hubby.controllerTest.UserTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.controller.routes.UserRoutes;
import com.hobbyFinder.hubby.models.dto.user.UserResponseDTO;
import com.hobbyFinder.hubby.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes de rota: obter usuário")
class GetUserTest {

    @Autowired
    private MockMvc driver;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserSeeder userSeeder;

    @Autowired
    private UserRepository userRepository;

    private UUID userId;
    private String token;

    @BeforeEach
    void setUp() throws Exception {
        userSeeder.seedUsers();
        userId = userRepository.findAll().get(0).getId();
        token = userSeeder.loginPrimeiroUser();
    }

    @Test
    @DisplayName("Obtém um usuário com sucesso")
    void testGetUserSuccess() throws Exception {
        String responseJsonString = driver.perform(get(UserRoutes.GET_USER_BY_ID,userId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserResponseDTO response = objectMapper.readValue(responseJsonString, UserResponseDTO.class);

        assertNotNull(response);
        assertEquals(userId, response.id());
        assertNotNull(response.username());
        assertNotNull(response.fullName());
        assertNotNull(response.photoDto());
    }

    @Test
    @DisplayName("Tenta obter um usuário com ID inexistente")
    void testGetUserNotFound() throws Exception {
        UUID nonExistentUserId = UUID.randomUUID();

        driver.perform(get(UserRoutes.GET_USER_BY_ID,nonExistentUserId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}