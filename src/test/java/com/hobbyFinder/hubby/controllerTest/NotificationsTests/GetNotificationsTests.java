package com.hobbyFinder.hubby.controllerTest.NotificationsTests;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    private GetUserLogged getUserLogged;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private User testUser;
    private Notification testNotification;

    @BeforeEach
    void setUp() {
        // Configuração do usuário de teste
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser = userRepository.save(testUser);

        // Configuração da notificação de teste
        testNotification = new Notification(testUser, "Test notification message", null);
        notificationRepository.save(testNotification);

        // Mock do GetUserLogged para retornar o usuário de teste
        when(getUserLogged.getUserLogged()).thenReturn(testUser);
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("Deve retornar notificações paginadas com sucesso - status 200")
    void getNotifications_ShouldReturnPaginatedNotifications() throws Exception {
        // Configura o mock do repositório para retornar uma página com múltiplas notificações
        Notification secondNotification = new Notification(testUser, "Second notification message", null);
        notificationRepository.save(secondNotification);
        
        mockMvc.perform(get("/api/notifications")
                .param("page", "0")
                .param("notificationsPerPage", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].message").value("Test notification message"))
            .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("Deve usar valores padrão quando parâmetros de paginação não são fornecidos - status 200")
    void getNotifications_ShouldUseDefaultPaginationValues() throws Exception {
        mockMvc.perform(get("/api/notifications"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[0].message").exists());
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("Deve retornar página vazia quando não houver notificações - status 200")
    void getNotifications_ShouldReturnEmptyPageWhenNoNotifications() throws Exception {
        notificationRepository.deleteAll();
        
        mockMvc.perform(get("/api/notifications"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    @DisplayName("Deve retornar status 403 quando usuário não estiver autenticado")
    void getNotifications_ShouldReturnForbiddenWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/notifications"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("Deve retornar apenas notificações do usuário autenticado")
    void getNotifications_ShouldReturnOnlyAuthenticatedUserNotifications() throws Exception {
        // Cria um segundo usuário com notificação
        User otherUser = new User();
        otherUser.setUsername("otheruser");
        otherUser.setPassword("password");
        userRepository.save(otherUser);

        Notification otherNotification = new Notification(otherUser, "Other user notification", null);
        notificationRepository.save(otherNotification);

        // Deve retornar apenas a notificação do usuário autenticado (testUser)
        mockMvc.perform(get("/api/notifications"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].message").value("Test notification message"))
            .andExpect(jsonPath("$.totalElements").value(1));
    }
}