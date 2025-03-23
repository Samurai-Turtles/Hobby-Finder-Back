package com.hobbyFinder.hubby.services.ServicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private final JavaMailSender javaMailSender;

  @Value("${spring.mail.username}")
  private String remetente;

  @Autowired
  public EmailService(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  public String enviarEmailTexto(
    String destinatario,
    String assunto,
    String mensagem
  ) {
    try {
      SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
      simpleMailMessage.setFrom(remetente);
      simpleMailMessage.setTo(destinatario);
      simpleMailMessage.setSubject(assunto);
      simpleMailMessage.setText(mensagem);
      javaMailSender.send(simpleMailMessage);
      return "Email enviado";
    } catch (Exception e) {
      return "Erro ao tentar enviar email: " + e.getLocalizedMessage();
    }
  }
}
