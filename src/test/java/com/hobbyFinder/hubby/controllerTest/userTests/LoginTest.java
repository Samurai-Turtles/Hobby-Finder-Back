package com.hobbyFinder.hubby.controllerTest.UserTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.controller.routes.UserRoutes;
import com.hobbyFinder.hubby.exception.AuthException.AuthExceptionsMessages;
import com.hobbyFinder.hubby.exception.CustomErrorType;
import com.hobbyFinder.hubby.models.dto.user.AuthDTO;
import com.hobbyFinder.hubby.models.dto.user.LoginResponseDTO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes de rota: login de usuário")
public class LoginTest {

    @Autowired
    private MockMvc driver;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserSeeder userSeeder;

    @BeforeEach
    void setUp() throws Exception {
        userSeeder = new UserSeeder(driver, objectMapper);
        userSeeder.seedUsers();
    }

    @AfterEach
    void tearDown() throws Exception {}

    @Transactional
    @Test
    @DisplayName("Usuário logado com sucesso por email")
    void testUsuarioLogadoComSucessoPorEmail() throws Exception {

        // Esse DTO utiliza o email com Login
        String responseJsonString = driver.perform(post(UserRoutes.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserConstants.primeiroAuthDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        LoginResponseDTO loginResponseDTO = objectMapper.readValue(responseJsonString, LoginResponseDTO.class);

        assertNotNull(loginResponseDTO);
    }

    @Transactional
    @Test
    @DisplayName("Usuário logado com sucesso por username")
    void testUsuarioLogadoComSucessoPorUsername() throws Exception {

        // Esse DTO utiliza o username com Login
        String responseJsonString = driver.perform(post(UserRoutes.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserConstants.segundoAuthDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        LoginResponseDTO loginResponseDTO = objectMapper.readValue(responseJsonString, LoginResponseDTO.class);

        assertNotNull(loginResponseDTO);
    }

    @Transactional
    @Test
    @DisplayName("Login com usuario inexistente")
    void testLoginUserInexistente() throws Exception {

        AuthDTO authDTOInexistente = new AuthDTO(
                UserConstants.LOGIN_INEXISTENTE,
                UserConstants.PASSWORD_NAO_UTILIZADA);

        String responseJsonString = driver.perform(post(UserRoutes.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDTOInexistente)))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType result = objectMapper.readValue(responseJsonString, CustomErrorType.class);
        assertEquals(AuthExceptionsMessages.INVALID_LOGIN_CREDENTIALS, result.getMessage());
    }
}
