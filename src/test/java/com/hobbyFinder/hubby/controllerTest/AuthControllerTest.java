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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

        assertEquals(AuthExceptionsMessages.INVALID_FIELD, result.getMessage());

    }

    @Transactional
    @Test
    @DisplayName("Registro com caracteres insuficientes")
    void testRegistroComNomeTamanho() throws Exception {

        RegisterDTO registerDTOTamanho = new RegisterDTO("victor@gmail.com", "lou","senha1234", UserRole.ADMIN);

        String responseJsonString = driver.perform(post("/auth/register")
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

        RegisterDTO registerDTOCarac = new RegisterDTO("victor@gmail.com", "!#&$","senha1234", UserRole.ADMIN);

        String responseJsonString = driver.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTOCarac)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType result = objectMapper.readValue(responseJsonString, CustomErrorType.class);
        assertEquals(AuthExceptionsMessages.INVALID_REGISTER_USERNAME, result.getMessage());
    }

    @Transactional
    @Test
    @DisplayName("Login com usuario inexistente")
    void testLoginUserInexistente() throws Exception {

        AuthDTO authDTOInexistente = new AuthDTO("esseUserNaoExiste@gmail.com", "senha1234");

        String responseJsonString = driver.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDTOInexistente)))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType result = objectMapper.readValue(responseJsonString, CustomErrorType.class);
        assertEquals(AuthExceptionsMessages.INVALID_LOGIN_CREDENTIALS, result.getMessage());
    }

    @Transactional
    @Test
    @DisplayName("Teste registro com username já existente")
    void testRegistroUsernameExistente() throws Exception {
        RegisterDTO registerUsernameExistenteDTO = new RegisterDTO(
                "novoEmail@gmail.com", "victor", "senha1234", UserRole.USER);

        String responseJsonString = driver.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerUsernameExistenteDTO)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType result = objectMapper.readValue(responseJsonString, CustomErrorType.class);
        assertEquals(AuthExceptionsMessages.USER_ALREADY_REGISTERED, result.getMessage());
    }

    @Transactional
    @Test
    @DisplayName("Teste registro com email já existente")
    void testRegistroEmailExistente() throws Exception {
        RegisterDTO registerEmailExistenteDTO = new RegisterDTO(
                "victor@gmail.com", "gabriel", "senha1234", UserRole.USER);

        String responseJsonString = driver.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerEmailExistenteDTO)))
                .andExpect(status().isBadRequest())
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
            "gabriel@gmail..com",           // Dois pontos seguidos
            "gabriel@-gmail.com",          // Domínio começando com "-"
            "gabriel@gmail.commmmmmmm"    // TLD muito grande
    })
    @DisplayName("Teste de registro com vários e-mails inválidos")
    void testRegistroEmailsInvalidos(String emailInvalido) throws Exception {
        RegisterDTO registerDTO = new RegisterDTO(
                emailInvalido, "gabriel", "senha1234", UserRole.USER);

        String responseJsonString = driver.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        CustomErrorType result = objectMapper.readValue(responseJsonString, CustomErrorType.class);
        assertEquals(AuthExceptionsMessages.INVALID_REGISTER_EMAIL, result.getMessage());
    }
}
