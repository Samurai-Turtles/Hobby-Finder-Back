//package com.hobbyFinder.hubby.services.ServicesImpl;
//
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//import com.hobbyFinder.hubby.models.entities.Email;
//
//@Service
//public class EmailService {
//
//  private final JavaMailSender mailSender;
//
//  public EmailService(JavaMailSender mailSender) {
//    this.mailSender = mailSender;
//  }
//
//  public void sendEmail(Email email) {
//    var message = new SimpleMailMessage();
//    message.setFrom("noreply@gmail.com");
//    message.setTo(email.to());
//    message.setSubject(email.subject());
//    message.setText(email.body());
//    mailSender.send(message);
//  }
//}
