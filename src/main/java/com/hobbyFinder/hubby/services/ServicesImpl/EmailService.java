package com.hobbyFinder.hubby.services.ServicesImpl;

import com.hobbyFinder.hubby.models.dto.email.EmailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    public void enviaEmail(EmailDto emailDto) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(remetente);
        simpleMailMessage.setTo(emailDto.email());
        simpleMailMessage.setSubject(emailDto.assunto());
        simpleMailMessage.setText(emailDto.texto());
        javaMailSender.send(simpleMailMessage);

    }
}