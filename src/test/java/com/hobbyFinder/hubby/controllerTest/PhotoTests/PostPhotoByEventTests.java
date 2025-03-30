package com.hobbyFinder.hubby.controllerTest.PhotoTests;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@AllArgsConstructor
@AutoConfigureMockMvc
@SpringBootTest()
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes de rota: Postar fotos em eventos.")
public class PostPhotoByEventTests {

}
