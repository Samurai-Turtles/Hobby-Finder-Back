package com.hobbyFinder.hubby.controllerTest.EventTests;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes de rota: obter evento por identificador de usuário")
public class GetEventByUserId {
}
