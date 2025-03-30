package com.hobbyFinder.hubby.controllerTest.UserTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.controller.routes.UserRoutes;
import com.hobbyFinder.hubby.models.dto.user.UserDTO;
import com.hobbyFinder.hubby.models.dto.user.UserPutDTO;
import com.hobbyFinder.hubby.models.enums.InterestEnum;
import com.hobbyFinder.hubby.models.enums.UserRole;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes de rota: atualização de usuário")
class UpdateTest {

    @Autowired
    private MockMvc driver;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserSeeder userSeeder;

    private String authToken;

    @BeforeEach
    void setUp() throws Exception {
        userSeeder.seedUsers();
        authToken = userSeeder.loginPrimeiroUser();
    }

    @Test
    @DisplayName("Deve atualizar o username com sucesso")
    void deveAtualizarUsernameComSucesso() throws Exception {
        String originalEmail = UserConstants.USER1_EMAIL;
        UserRole originalRole = UserRole.USER;
        UserPutDTO updateRequest = new UserPutDTO(
                null,
                "novoUsername",
                null,
                null,
                null,
                null
        );

        String responseJson = performUpdateRequest(updateRequest)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserDTO responseDto = objectMapper.readValue(responseJson, UserDTO.class);

        assertEquals("novoUsername", responseDto.username());
        assertEquals(originalEmail, responseDto.email());
        assertEquals(originalRole, responseDto.role());
    }

    @Test
    @DisplayName("Deve atualizar o email com sucesso")
    void deveAtualizarEmailComSucesso() throws Exception {
        String originalUsername = UserConstants.USER1_USERNAME;
        UserRole originalRole = UserRole.USER;
        UserPutDTO updateRequest = new UserPutDTO(
                "novo.email@teste.com",
                null,
                null,
                null,
                null,
                null
        );

        String responseJson = performUpdateRequest(updateRequest)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserDTO responseDto = objectMapper.readValue(responseJson, UserDTO.class);

        assertEquals("novo.email@teste.com", responseDto.email());
        assertEquals(originalUsername, responseDto.username());
        assertEquals(originalRole, responseDto.role());
    }

    @Test
    @DisplayName("Deve atualizar múltiplos campos simultaneamente")
    void deveAtualizarMultiplosCamposSimultaneamente() throws Exception {
        UserPutDTO updateRequest = new UserPutDTO(
                "novo.email@teste.com",
                "novoUsername",
                "NovaSenha123",
                "Novo Nome",
                "Nova bio",
                List.of(InterestEnum.SPORT, InterestEnum.ANIME)
        );

        String responseJson = performUpdateRequest(updateRequest)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserDTO responseDto = objectMapper.readValue(responseJson, UserDTO.class);

        assertEquals("novo.email@teste.com", responseDto.email());
        assertEquals("novoUsername", responseDto.username());
    }

    @Test
    @DisplayName("Deve atualizar a biografia com sucesso")
    void deveAtualizarBiografiaComSucesso() throws Exception {
        String originalEmail = UserConstants.USER1_EMAIL;
        UserRole originalRole = UserRole.USER;
        UserPutDTO updateRequest = new UserPutDTO(
                null,
                null,
                null,
                null,
                "Nova bio do usuário",
                null
        );

        String responseJson = performUpdateRequest(updateRequest)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserDTO responseDto = objectMapper.readValue(responseJson, UserDTO.class);

        assertEquals(originalEmail, responseDto.email());
        assertEquals(originalRole, responseDto.role());
    }

    @Test
    @DisplayName("Deve atualizar o nome completo com sucesso")
    void deveAtualizarNomeCompletoComSucesso() throws Exception {
        String originalEmail = UserConstants.USER1_EMAIL;
        UserRole originalRole = UserRole.USER;
        UserPutDTO updateRequest = new UserPutDTO(
                null,
                null,
                null,
                "Novo Nome Completo",
                null,
                null
        );

        String responseJson = performUpdateRequest(updateRequest)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserDTO responseDto = objectMapper.readValue(responseJson, UserDTO.class);

        assertEquals(originalEmail, responseDto.email());
        assertEquals(originalRole, responseDto.role());
    }

    private org.springframework.test.web.servlet.ResultActions performUpdateRequest(UserPutDTO updateRequest) throws Exception {
        return driver.perform(put(UserRoutes.PUT_AUTH_USER)
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)));
    }
}