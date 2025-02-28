package com.hobbyFinder.hubby.services.ServicesImpl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hobbyFinder.hubby.models.dto.user.UserDTO;
import com.hobbyFinder.hubby.repositories.UserRepository;
import com.hobbyFinder.hubby.services.IServices.UserInterface;

@Service
public class UserService implements UserInterface {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EmailService emailService;

  @Override
  public UserDTO getUser(UUID uuid) {
    return userRepository
      .findById(uuid)
      .map(user ->
        new UserDTO(user.getEmail(), user.getUsername(), user.getRole())
      )
      .orElseThrow(() -> new UsernameNotFoundException("User não encontrado."));
  }

  public void enviarEmailCadastro(String email) {
    emailService.enviarEmailTexto(
      email,
      "Novo usuário cadastrado",
      "Você está recebendo um email de cadastro"
    );
  }
}
