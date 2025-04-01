package com.hobbyFinder.hubby.controllerTest.NotificationsTests;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hobbyFinder.hubby.models.enums.NotificationEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.hobbyFinder.hubby.models.entities.Notification;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.repositories.NotificationRepository;
import com.hobbyFinder.hubby.repositories.UserRepository;
import com.hobbyFinder.hubby.util.GetUserLogged;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes de rota: Obter notificações pelo usuário autenticado")
public class GetNotificationsTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private GetUserLogged getUserLogged; // Mock for getting the logged user

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private NotificationRepository notificationRepository;

  private User testUser;
  private Notification testNotification;

  @BeforeEach
  void setUp() {
    testUser = new User();
    testUser.setUsername("testuser");
    testUser.setPassword("password");
    testUser = userRepository.save(testUser);

    testNotification =
      new Notification(testUser, "Test notification message", null, testUser.getId(), NotificationEnum.PARTICIPATION);
    notificationRepository.save(testNotification);

    // Mock do GetUserLogged para retornar o usuário de teste
    when(getUserLogged.getUserLogged()).thenReturn(testUser);
  }

  @Test
  @WithMockUser(username = "testuser")
  @DisplayName(
    "Deve retornar página vazia quando não houver notificações - status 200"
  )
  void getNotifications_ShouldReturnEmptyPageWhenNoNotifications()
    throws Exception {
    notificationRepository.deleteAll();

    mockMvc
      .perform(get("/api/notifications"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content").isArray())
      .andExpect(jsonPath("$.content").isEmpty());
  }

  @Test
  @DisplayName(
    "Deve retornar status 403 quando usuário não estiver autenticado"
  )
  void getNotifications_ShouldReturnForbiddenWhenNotAuthenticated()
    throws Exception {
    mockMvc
      .perform(get("/api/notifications"))
      .andExpect(status().isForbidden());
  }
}
