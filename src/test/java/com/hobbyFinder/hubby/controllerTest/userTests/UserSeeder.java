package com.hobbyFinder.hubby.controllerTest.userTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyFinder.hubby.controller.routes.UserRoutes;
import com.hobbyFinder.hubby.models.dto.user.LoginResponseDTO;
import com.hobbyFinder.hubby.models.dto.user.RegisterDTO;
import jakarta.transaction.Transactional;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class UserSeeder {

    private final MockMvc driver;
    private final ObjectMapper objectMapper;

    public UserSeeder(MockMvc driver, ObjectMapper objectMapper) {
        this.driver = driver;
        this.objectMapper = objectMapper;
    }

    public void seedUsers() throws Exception {
        cadastrarUsuario(UserConstants.primeiroRegistroDto);
        cadastrarUsuario(UserConstants.segundoRegistroDto);
    }

    protected void cadastrarUsuario(RegisterDTO request) throws Exception {

        driver.perform(post(UserRoutes.POST_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }


    public String loginPrimeiroUser() throws Exception {
        String responseJsonStringLogin = driver.perform(post(UserRoutes.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserConstants.primeiroAuthDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseJsonStringLogin, LoginResponseDTO.class).token();
    }


    public String loginSegundoUser() throws Exception {
        String responseJsonStringLogin = driver.perform(post(UserRoutes.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserConstants.segundoAuthDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(responseJsonStringLogin, LoginResponseDTO.class).token();
    }
    
}
