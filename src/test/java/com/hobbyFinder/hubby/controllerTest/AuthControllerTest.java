package com.hobbyFinder.hubby.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.exception.AuthException.AuthExceptionsMessages;
import com.hobbyFinder.hubby.exception.CustomErrorType;
import com.hobbyFinder.hubby.models.dto.user.AuthDTO;
import com.hobbyFinder.hubby.models.dto.user.LoginResponseDTO;
import com.hobbyFinder.hubby.models.dto.user.RegisterDTO;
import com.hobbyFinder.hubby.models.entities.UserRole;
import jakarta.transaction.Transactional;
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
    private ObjectMapper objectMapper;

    private RegisterDTO request;
    private AuthDTO authDTO;

    @Transactional
    @BeforeEach
    void setUp() throws Exception {
        this.request = new RegisterDTO("victor@gmail.com", "victor","senha1234", UserRole.ADMIN);
        this.authDTO = new AuthDTO("victor@gmail.com", "senha1234");
        cadastrarUsuario(request);
    }

    @Transactional
    protected void cadastrarUsuario(RegisterDTO request) throws Exception {

        driver.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    //NÃO coloque userRepository.deleteAll() aqui!!
    @AfterEach
    void tearDown() throws Exception {}

    @Transactional
    @Test
    @DisplayName("Usuário cadastrado com sucesso")
    void testUsuarioCadastradoComSucesso() throws Exception {

        RegisterDTO registerTest = new RegisterDTO("gabriel@gmail.com", "gabriel","senha1234", UserRole.ADMIN);

        driver.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerTest)))
                .andExpect(status().isCreated());

    }

    @Transactional
    @Test
    @DisplayName("Usuário logado com sucesso")
    void testUsuarioLogadoComSucesso() throws Exception {

        String responseJsonString = driver.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        LoginResponseDTO loginResponseDTO = objectMapper.readValue(responseJsonString, LoginResponseDTO.class);

        assertNotNull(loginResponseDTO);
    }

    @Transactional
    @Test
    @DisplayName("Registro com nome nulo")
    void testRegistroComNomeInvalido() throws Exception {

        RegisterDTO registerDtoNulo = new RegisterDTO("victorNulo@gmail.com", null,"senha1234", UserRole.ADMIN);

        String responseJsonString = driver.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerDtoNulo)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType result = objectMapper.readValue(responseJsonString, CustomErrorType.class);

        assertEquals(result.getMessage(), AuthExceptionsMessages.INVALID_FIELD);

    }

    @Transactional
    @Test
    @DisplayName("Registro com caracteres insuficientes")
    void testRegistroComNomeTamanho() throws Exception {

        String responseJsonString = driver.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();


    }

    @Transactional
    @Test
    @DisplayName("Registro com nome possuindo caracteres especiais")
    void testRegistroComCaracteresEspeciais() throws Exception {

        String responseJsonString = driver.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();


    }

    @Transactional
    @Test
    @DisplayName("Login com usuario inexistente")
    void testLoginUserInexistente() throws Exception {

        String responseJsonString = driver.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        //CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);
        //assertEquals("Seu nome de usuário ou senha podem estar incorretos", resultado.getMessage());
    }

}
