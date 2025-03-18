package com.hobbyFinder.hubby.controllerTest.UserTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.controller.routes.UserRoutes;
import com.hobbyFinder.hubby.exception.AuthException.AuthExceptionsMessages;
import com.hobbyFinder.hubby.exception.CustomErrorType;
import com.hobbyFinder.hubby.models.dto.user.RegisterDTO;
import com.hobbyFinder.hubby.models.entities.UserRole;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes de rota: registro de usuário")
public class RegisterTest {

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
    @DisplayName("Usuário cadastrado com sucesso")
    void testUsuarioCadastradoComSucesso() throws Exception {

        RegisterDTO registerTest = new RegisterDTO(
                UserConstants.EMAIL_NAO_UTILIZADO,
                UserConstants.USERNAME_NAO_UTILIZADO,
                UserConstants.PASSWORD_NAO_UTILIZADA,
                UserConstants.FULL_NAME_NAO_UTILIZADO);

        driver.perform(post(UserRoutes.POST_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerTest)))
                .andExpect(status().isCreated());

    }

    @Transactional
    @Test
    @DisplayName("Registro com nome nulo")
    void testRegistroComNomeInvalido() throws Exception {

        RegisterDTO registerDtoNulo = new RegisterDTO(
                UserConstants.EMAIL_NAO_UTILIZADO,
                null,
                UserConstants.PASSWORD_NAO_UTILIZADA,
                UserConstants.FULL_NAME_NAO_UTILIZADO);

        String responseJsonString = driver.perform(post(UserRoutes.POST_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDtoNulo)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType result = objectMapper.readValue(responseJsonString, CustomErrorType.class);

        assertEquals(AuthExceptionsMessages.INVALID_FIELD, result.getMessage());

    }

    @Transactional
    @Test
    @DisplayName("Registro com caracteres insuficientes")
    void testRegistroComNomeTamanho() throws Exception {

        RegisterDTO registerDTOTamanho = new RegisterDTO(
                UserConstants.EMAIL_NAO_UTILIZADO,
                UserConstants.USERNAME_TAMANHO_INVALIDO,
                UserConstants.PASSWORD_NAO_UTILIZADA,
                UserConstants.FULL_NAME_NAO_UTILIZADO);

        String responseJsonString = driver.perform(post(UserRoutes.POST_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTOTamanho)))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType result = objectMapper.readValue(responseJsonString, CustomErrorType.class);
        assertEquals(AuthExceptionsMessages.INVALID_REGISTER_USERNAME_SIZE, result.getMessage());
    }

    @Transactional
    @Test
    @DisplayName("Registro com nome possuindo caracteres especiais")
    void testRegistroComCaracteresEspeciais() throws Exception {

        RegisterDTO registerDTOCarac = new RegisterDTO(
                UserConstants.EMAIL_NAO_UTILIZADO,
                UserConstants.USERNAME_INVALIDO,
                UserConstants.PASSWORD_NAO_UTILIZADA,
                UserConstants.FULL_NAME_NAO_UTILIZADO);

        String responseJsonString = driver.perform(post(UserRoutes.POST_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTOCarac)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType result = objectMapper.readValue(responseJsonString, CustomErrorType.class);
        assertEquals(AuthExceptionsMessages.INVALID_REGISTER_USERNAME, result.getMessage());
    }

    @Transactional
    @Test
    @DisplayName("Teste registro com username já existente")
    void testRegistroUsernameExistente() throws Exception {
        RegisterDTO registerUsernameExistenteDTO = new RegisterDTO(
                UserConstants.EMAIL_NAO_UTILIZADO,
                UserConstants.USER1_USERNAME,
                UserConstants.PASSWORD_NAO_UTILIZADA,
                UserConstants.FULL_NAME_NAO_UTILIZADO);

        String responseJsonString = driver.perform(post(UserRoutes.POST_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUsernameExistenteDTO)))
                .andExpect(status().isConflict())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType result = objectMapper.readValue(responseJsonString, CustomErrorType.class);
        assertEquals(AuthExceptionsMessages.USER_ALREADY_REGISTERED, result.getMessage());
    }

    @Transactional
    @Test
    @DisplayName("Teste registro com email já existente")
    void testRegistroEmailExistente() throws Exception {
        RegisterDTO registerEmailExistenteDTO = new RegisterDTO(
                UserConstants.USER1_EMAIL,
                UserConstants.USERNAME_NAO_UTILIZADO,
                UserConstants.PASSWORD_NAO_UTILIZADA,
                UserConstants.FULL_NAME_NAO_UTILIZADO);

        String responseJsonString = driver.perform(post(UserRoutes.POST_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerEmailExistenteDTO)))
                .andExpect(status().isConflict())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType result = objectMapper.readValue(responseJsonString, CustomErrorType.class);
        assertEquals(AuthExceptionsMessages.EMAIL_ALREADY_REGISTERED, result.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "@gmail.com",                          // Sem nome de usuário
            "gabriel@",                           // Sem domínio
            "gabriel@gmail",                     // Sem TLD
            "gabriel@@gmail.com",               // Dois @
            "gabriel.gmail.com",               // Sem @
            "gabriel@.com",                   // Domínio inválido
            "gabriel@com",                   // Apenas TLD inválido
            "gabriel@gmail.commmmmmmm"    // TLD muito grande
    })
    @DisplayName("Teste de registro com vários e-mails inválidos")
    void testRegistroEmailsInvalidos(String emailInvalido) throws Exception {
        RegisterDTO registerDTO = new RegisterDTO(
                emailInvalido,
                UserConstants.USERNAME_NAO_UTILIZADO,
                UserConstants.PASSWORD_NAO_UTILIZADA,
                UserConstants.FULL_NAME_NAO_UTILIZADO);

        String responseJsonString = driver.perform(post(UserRoutes.POST_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType result = objectMapper.readValue(responseJsonString, CustomErrorType.class);
        assertEquals(AuthExceptionsMessages.INVALID_REGISTER_EMAIL, result.getMessage());
    }
}