package com.hobbyFinder.hubby.controllerTest.NotificationsTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.hobbyFinder.hubby.models.enums.NotificationEnum;
import com.hobbyFinder.hubby.services.ServicesImpl.EmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.hobbyFinder.hubby.models.dto.notifications.NotificationDto;
import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.models.entities.Notification;
import com.hobbyFinder.hubby.models.entities.Participation;
import com.hobbyFinder.hubby.models.entities.Photo;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.models.enums.ParticipationPosition;
import com.hobbyFinder.hubby.models.enums.UserRole;
import com.hobbyFinder.hubby.repositories.NotificationRepository;
import com.hobbyFinder.hubby.services.IServices.UserInterface;
import com.hobbyFinder.hubby.services.ServicesImpl.NotificationService;
import com.hobbyFinder.hubby.util.GetUserLogged;

@ContextConfiguration(classes = {NotificationService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
public class NotificationServiceTest {

    @MockBean
    private GetUserLogged getUserLogged;

    @MockBean
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationService notificationService;

    @MockBean
    private UserInterface userInterface;

    @MockBean
    private EmailService emailService;

    @Test
    @DisplayName(
            "Testar notificação de alteração de evento; deve notificar todos os participantes"
    )
    void testNotifyChangeEvent_deveNotificarTodosParticipantes() {
        // Arrange
        Event evento = mock(Event.class);
        User usuario1 = new User();
        usuario1.setId(UUID.randomUUID());
        User usuario2 = new User();
        usuario2.setId(UUID.randomUUID());
        Participation participacao1 = new Participation();
        participacao1.setIdUser(usuario1.getId());
        Participation participacao2 = new Participation();
        participacao2.setIdUser(usuario2.getId());
        List<Participation> participacoes = List.of(participacao1, participacao2);
        when(evento.getParticipations()).thenReturn(participacoes);
        when(userInterface.getUser(usuario1.getId())).thenReturn(usuario1);
        when(userInterface.getUser(usuario2.getId())).thenReturn(usuario2);

        // Act
        notificationService.notifyChangeEvent(evento);

        // Assert
        verify(notificationRepository, times(2)).save(any(Notification.class));
    }

    @Test
    @DisplayName("Testar getNotifications(Pageable); deve retornar lista vazia")
    void testGetNotifications_deveRetornarListaVazia() {
        // Arrange
        when(
                notificationRepository.findByUserId(
                        Mockito.<UUID>any(),
                        Mockito.<Pageable>any()
                )
        )
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        Photo foto = new Photo();
        foto.setExtension("Extensão");
        foto.setId(UUID.randomUUID());
        foto.setSaved(true);

        User usuario = new User();
        usuario.setBio("Bio");
        usuario.setEmail("jane.doe@example.org");
        usuario.setFullName("Dr Jane Doe");
        usuario.setId(UUID.randomUUID());
        usuario.setInterests(new ArrayList<>());
        usuario.setNotifications(new ArrayList<>());
        usuario.setParticipations(new ArrayList<>());
        usuario.setPassword("iloveyou");
        usuario.setPhoto(foto);
        usuario.setRequests(new ArrayList<>());
        usuario.setRole(UserRole.ADMIN);
        usuario.setStars(10.0d);
        usuario.setUsername("janedoe");
        when(getUserLogged.getUserLogged()).thenReturn(usuario);

        // Act
        Page<NotificationDto> notifications = notificationService.getNotifications(
                null
        );

        // Assert
        verify(notificationRepository).findByUserId(isA(UUID.class), isNull());
        verify(getUserLogged).getUserLogged();
        assertTrue(notifications instanceof PageImpl);
        assertTrue(notifications.toList().isEmpty());
    }

  @Test
  @DisplayName(
    "Testar notificação de solicitação; deve notificar o organizador da solicitação"
  )
  void testNotifySolicitation_deveNotificarOrganizador() {
    // Arrange
    User usuario = new User();
    usuario.setId(UUID.randomUUID());
    usuario.setUsername("Usuário Teste");

    // Criar evento e configurar participações
    Event evento = new Event();
    evento.setName("Evento Exemplo");
    evento.setParticipations(new ArrayList<>()); // Inicializar lista de participações

    // Criar e configurar a participação do organizador (usando ParticipationPosition)
    Participation participacao = new Participation();
    participacao.setIdUser(UUID.randomUUID());
    participacao.setIdEvent(evento.getId());
    participacao.setPosition(ParticipationPosition.CREATOR); // Usar CREATOR para definir como organizador

    // Adicionar a participação ao evento
    evento.getParticipations().add(participacao);

    // Simular a interface de usuário para retornar o usuário
    when(userInterface.getUser(any(UUID.class))).thenReturn(usuario);

    // Act
    notificationService.notifyConfirmParticipation(usuario, evento, participacao);

    // Assert
    verify(notificationRepository).save(any(Notification.class)); // Garantir que uma notificação foi salva
  }

  @Test
  @DisplayName(
    "Testar notificação de confirmação de participação; deve notificar o organizador da confirmação"
  )
  void testNotifyConfirmParticipation_deveNotificarOrganizador() {
    // Arrange
    User usuario = new User();
    usuario.setId(UUID.randomUUID());
    usuario.setUsername("Usuário Teste");
    Event evento = new Event();
    evento.setName("Evento Exemplo");
    evento.setParticipations(new ArrayList<>()); // Inicializar lista de participações

    // Criar participação do organizador
    Participation participacao = new Participation();
    participacao.setIdUser(UUID.randomUUID());
    participacao.setPosition(ParticipationPosition.CREATOR); // Usar CREATOR para definir como organizador
    evento.getParticipations().add(participacao);

    // Simular a interface de usuário para retornar o usuário
    when(userInterface.getUser(any(UUID.class))).thenReturn(usuario);

    // Act
    notificationService.notifyConfirmParticipation(usuario, evento, participacao);

    // Assert
    verify(notificationRepository).save(any(Notification.class)); // Garantir que uma notificação foi salva
  }

  @Test
  @DisplayName("Testar criação de notificação; deve retornar lista não vazia")
  void testCreateNotification_deveRetornarListaNaoVazia() {
    // Arrange
    User usuario = new User();
    usuario.setId(UUID.randomUUID());
    usuario.setUsername("Usuário Teste");
    usuario.setPhoto(new Photo());

    String mensagem = "Mensagem de Notificação Teste";
    Notification notificacao = new Notification(
      usuario,
      mensagem,
      usuario.getPhoto(),
      null, null,
      NotificationEnum.PARTICIPATION
    );

    when(getUserLogged.getUserLogged()).thenReturn(usuario);
    when(notificationRepository.save(any(Notification.class)))
      .thenReturn(notificacao);

    // Simular o repositório para retornar uma lista não vazia
    List<Notification> listaNotificacoes = new ArrayList<>();
    listaNotificacoes.add(notificacao);

    notificationService.postNotification(usuario, usuario.getPhoto(), mensagem, null, null, NotificationEnum.PARTICIPATION);

        // Assert
        verify(notificationRepository).save(any(Notification.class));
    }

}
