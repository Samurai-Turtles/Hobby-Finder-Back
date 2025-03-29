package com.hobbyFinder.hubby.services.ServicesImpl;

import com.hobbyFinder.hubby.models.dto.email.EmailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void enviaEmail(EmailDto emailDto) {
        var message = new SimpleMailMessage();
        message.setFrom("suportehobbyfinder@gmail.com");
        message.setTo(emailDto.email());
        message.setSubject("Recuperação de senha - HobbyFinder");
        message.setText(emailDto.texto());
        javaMailSender.send(message);
    }
}