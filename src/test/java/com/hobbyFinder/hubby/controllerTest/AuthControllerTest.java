package com.hobbyFinder.hubby.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.exception.AuthException.AuthExceptionsMessages;
import com.hobbyFinder.hubby.exception.CustomErrorType;
import com.hobbyFinder.hubby.models.dto.user.AuthDTO;
import com.hobbyFinder.hubby.models.dto.user.LoginResponseDTO;
import com.hobbyFinder.hubby.models.dto.user.RegisterDTO;
import com.hobbyFinder.hubby.models.entities.UserRole;
import com.hobbyFinder.hubby.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes de rota registro/autenticação")
public class AuthControllerTest {

    @Autowired
    private MockMvc driver;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {}

    @AfterEach
    void tearDown() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Usuário cadastrado com sucesso")
    void testUsuarioCadastradoComSucesso() throws Exception {
        RegisterDTO request = new RegisterDTO("victor@gmail.com", "senha1234", UserRole.ADMIN);

        driver.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("Usuário logado com sucesso")
    void testUsuarioLogadoComSucesso() throws Exception {
        AuthDTO authDTO = new AuthDTO("victor@gmail.com", "senha1234");

        String responseJsonString = driver.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        LoginResponseDTO loginResponseDTO = objectMapper.readValue(responseJsonString, LoginResponseDTO.class);

        assertNotNull(loginResponseDTO);
    }

    @Test
    @DisplayName("Registro com nome nulo")
    void testRegistroComNomeInvalido() throws Exception {
        RegisterDTO request = new RegisterDTO("", "senha1234", UserRole.ADMIN);

        String responseJsonString = driver.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType result = objectMapper.readValue(responseJsonString, CustomErrorType.class);

        assertEquals(result.getMessage(), AuthExceptionsMessages.INVALID_REGISTER_CREDENTIALS);

    }

    @Test
    @DisplayName("Registro com caracteres insuficientes")
    void testRegistroComNomeTamanho() throws Exception {
        RegisterDTO request = new RegisterDTO("", "senha1234", UserRole.ADMIN);

        String responseJsonString = driver.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();


    }

    @Test
    @DisplayName("Registro com nome possuindo caracteres especiais")
    void testRegistroComCaracteresEspeciais() throws Exception {
        RegisterDTO request = new RegisterDTO("", "senha1234", UserRole.ADMIN);

        String responseJsonString = driver.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();


    }

    @Test
    @DisplayName("Login com usuario inexistente")
    void testLoginUserInexistente() throws Exception {
        AuthDTO authDTO = new AuthDTO("username", "password");

        String responseJsonString = driver.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        //CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);
        //assertEquals("Seu nome de usuário ou senha podem estar incorretos", resultado.getMessage());
    }

}
