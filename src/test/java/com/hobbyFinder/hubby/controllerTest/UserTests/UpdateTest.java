package com.hobbyFinder.hubby.controllerTest.UserTests;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.controller.routes.UserRoutes;
import com.hobbyFinder.hubby.exception.AuthException.AuthExceptionsMessages;
import com.hobbyFinder.hubby.exception.CustomErrorType;
import com.hobbyFinder.hubby.models.dto.user.LoginResponseDTO;
import com.hobbyFinder.hubby.models.dto.user.UserPutDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes de rota: update de usuário")
public class UpdateTest {

    @Autowired
    private UserSeeder userSeeder;
    @Autowired
    private MockMvc driver;
    @Autowired
    private ObjectMapper objectMapper;

    private UserPutDTO userPutDto;

    @BeforeEach
    void setUp() throws Exception {
        userSeeder = new UserSeeder(driver, objectMapper);
        userSeeder.seedUsers();
    }

    @AfterEach
    void tearDown() throws Exception {}



    @Nested()
    @DisplayName("Testes de edição de username")
    class UsernameUpdateTest{

        @Transactional
        @Test
        @DisplayName("Teste de username válido")
        void testEditaUsernameValido() throws Exception {
            String token = userSeeder.loginPrimeiroUser();

            userPutDto = new UserPutDTO(null, UserConstants.USERNAME_NAO_UTILIZADO, null, null, null, null);

            String responseJsonString = driver.perform(put(UserRoutes.PUT_AUTH_USER)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .content(objectMapper.writeValueAsString(userPutDto)))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            String novoToken = objectMapper.readValue(responseJsonString, LoginResponseDTO.class).token();

            assertNotNull(novoToken);
        }

        @Transactional
        @Test
        @DisplayName("Teste de username tamanho invalido")
        void testEditaUsernameTamanhoInvalido() throws Exception {
            String token = userSeeder.loginPrimeiroUser();

            userPutDto = new UserPutDTO(null, UserConstants.USERNAME_TAMANHO_INVALIDO, null, null, null, null);

            String responseJsonString = driver.perform(put(UserRoutes.PUT_AUTH_USER)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .content(objectMapper.writeValueAsString(userPutDto)))
                    .andExpect(status().isUnprocessableEntity())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            assertEquals(AuthExceptionsMessages.INVALID_REGISTER_USERNAME_SIZE, resultado.getMessage());
        }

        @Transactional
        @Test
        @DisplayName("Teste de username invalido")
        void testEditaUsernameInvalido() throws Exception {
            String token = userSeeder.loginPrimeiroUser();

            userPutDto = new UserPutDTO(null, UserConstants.USERNAME_INVALIDO, null, null, null, null);

            String responseJsonString = driver.perform(put(UserRoutes.PUT_AUTH_USER)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .content(objectMapper.writeValueAsString(userPutDto)))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            assertEquals(AuthExceptionsMessages.INVALID_REGISTER_USERNAME, resultado.getMessage());
        }


        @Transactional
        @Test
        @DisplayName("Teste de username já existe")
        void testEditaUsernameJaExistente() throws Exception {
            String token = userSeeder.loginPrimeiroUser();

            userPutDto = new UserPutDTO(null, UserConstants.USER2_USERNAME, null, null, null, null);

            String responseJsonString = driver.perform(put(UserRoutes.PUT_AUTH_USER)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .content(objectMapper.writeValueAsString(userPutDto)))
                    .andExpect(status().isConflict())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            assertEquals(AuthExceptionsMessages.USER_ALREADY_REGISTERED, resultado.getMessage());
        }

    }

    @Nested()
    @DisplayName("Testes de edição de email")
    class EmailUpdateTest{

        @Transactional
        @Test
        @DisplayName("Teste de username válido")
        void testEditaEmailValido() throws Exception {
            String token = userSeeder.loginPrimeiroUser();

            userPutDto = new UserPutDTO(
                    UserConstants.EMAIL_NAO_UTILIZADO, null, null, null, null, null);

            String responseJsonString = driver.perform(put(UserRoutes.PUT_AUTH_USER)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .content(objectMapper.writeValueAsString(userPutDto)))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            String novoToken = objectMapper.readValue(responseJsonString, LoginResponseDTO.class).token();

            assertNotNull(novoToken);
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
        @DisplayName("Teste de email invalido")
        void testEditaEmailInvalido(String emailInvalido) throws Exception {
            String token = userSeeder.loginPrimeiroUser();

            userPutDto = new UserPutDTO(emailInvalido, null, null, null, null, null);

            String responseJsonString = driver.perform(put(UserRoutes.PUT_AUTH_USER)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .content(objectMapper.writeValueAsString(userPutDto)))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            assertEquals(AuthExceptionsMessages.INVALID_REGISTER_EMAIL, resultado.getMessage());
        }

        @Transactional
        @Test
        @DisplayName("Teste de email que já existe")
        void testEditaEmailJaExistente() throws Exception {
            String token = userSeeder.loginPrimeiroUser();

            userPutDto = new UserPutDTO(UserConstants.USER1_EMAIL, null, null, null, null, null);

            String responseJsonString = driver.perform(put(UserRoutes.PUT_AUTH_USER)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .content(objectMapper.writeValueAsString(userPutDto)))
                    .andExpect(status().isConflict())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            assertEquals(AuthExceptionsMessages.EMAIL_ALREADY_REGISTERED, resultado.getMessage());
        }
    }

    @Nested()
    @DisplayName("Teste de edição de senha")
    class SenhaUpdateTest{
        @Transactional
        @Test
        @DisplayName("Teste de senha válido")
        void testEditaUsernameValido() throws Exception {
            String token = userSeeder.loginPrimeiroUser();
            userPutDto = new UserPutDTO(null, null, UserConstants.PASSWORD_NAO_UTILIZADA, null, null, null);

            driver.perform(put(UserRoutes.PUT_AUTH_USER)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .content(objectMapper.writeValueAsString(userPutDto)))
                    .andExpect(status().isOk());
        }

        @Transactional
        @Test
        @DisplayName("Teste de senha invalida")
        void testEditaSenhaInvalida() throws Exception {
            String token = userSeeder.loginPrimeiroUser();

            userPutDto = new UserPutDTO(null, null, UserConstants.PASSWORD_INVALIDA, null, null, null);

            String responseJsonString = driver.perform(put(UserRoutes.PUT_AUTH_USER)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .content(objectMapper.writeValueAsString(userPutDto)))
                    .andExpect(status().isUnprocessableEntity())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            assertEquals(AuthExceptionsMessages.INVALID_REGISTER_PASSWORD_SIZE, resultado.getMessage());
        }
    }

    @Nested()
    @DisplayName("Teste para edição geral de usuário")
    class TagUpdateTest{

        @Test
        @DisplayName("Teste para edição válida")
        void testUpdateValido() throws Exception {
            String token = userSeeder.loginPrimeiroUser();

            String responseJsonString = driver.perform(put(UserRoutes.PUT_AUTH_USER)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .content(objectMapper.writeValueAsString(UserConstants.userPutDto)))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
            String novoToken = objectMapper.readValue(responseJsonString, LoginResponseDTO.class).token();

            assertNotNull(novoToken);
        }


    }
}
