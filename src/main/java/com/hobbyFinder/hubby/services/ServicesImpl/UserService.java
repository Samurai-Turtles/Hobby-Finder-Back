package com.hobbyFinder.hubby.services.ServicesImpl;

import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hobbyFinder.hubby.exception.TagInvalidaException;
import com.hobbyFinder.hubby.exception.NotFound.UserNotFoundException;
import com.hobbyFinder.hubby.models.dto.photos.PhotoDto;
import com.hobbyFinder.hubby.models.dto.user.UserDTO;
import com.hobbyFinder.hubby.models.dto.user.UserPutDTO;
import com.hobbyFinder.hubby.models.dto.user.UserResponseDTO;
import com.hobbyFinder.hubby.models.entities.Email;
import com.hobbyFinder.hubby.models.entities.Photo;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.models.enums.InterestEnum;
import com.hobbyFinder.hubby.repositories.UserRepository;
import com.hobbyFinder.hubby.services.IServices.UserInterface;
import com.hobbyFinder.hubby.services.Validation.UserValidator;
import com.hobbyFinder.hubby.util.GetUserLogged;

@Service
public class UserService implements UserInterface {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  @Lazy
  private GetUserLogged userLogged;

  @Autowired
  private UserValidator userValidator;

  @Autowired
  private EmailService emailService;

  private final Set<InterestEnum> validInterests = Set.of(
    InterestEnum.values()
  );

  @Override
  public UserResponseDTO getUserResponse(UUID uuid) {
    User user = getUser(uuid);
    Photo photo = user.getPhoto();
    PhotoDto photoDto = new PhotoDto(
      photo.getId(),
      photo.getExtension(),
      photo.isSaved()
    );
    return new UserResponseDTO(
      user.getId(),
      user.getUsername(),
      user.getFullName(),
      user.getBio(),
      user.getInterests(),
      photoDto,
      user.getStars()
    );
  }

  @Override
  public User getUser(UUID uuid) {
    return userRepository
      .findById(uuid)
      .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));
  }

  @Override
  public void deleteUser() {
    User user = userLogged.getUserLogged();
    userRepository.delete(user);
    userRepository.flush();
  }

  public UserDTO updateUser(UserPutDTO request) {
    User user = userLogged.getUserLogged();

    if (request.username() != null) {
      userValidator.validaUsername(request.username());
      user.setUsername(request.username());
    }

    if (request.email() != null) {
      userValidator.validaEmail(request.email());
      user.setEmail(request.email());
    }

    if (request.password() != null) {
      userValidator.validaPassword(request.password());
      user.setPassword(new BCryptPasswordEncoder().encode(request.password()));
    }

    if (request.interests() != null) {
      if (!validInterests.containsAll(request.interests())) {
        throw new TagInvalidaException("Tag invalida");
      }
      user.setInterests(request.interests());
    }

    if (request.bio() != null) {
      user.setBio(request.bio());
    }

    if (request.name() != null) {
      user.setFullName(request.name());
    }
    userRepository.save(user);
    return new UserDTO(user.getEmail(), user.getUsername(), user.getRole());
  }

  @Override
  public void updateUserAvaliation(UUID idUser, double stars) {
    User user = getUser(idUser);
    user.setStars(stars);
    this.userRepository.save(user);
  }

  public void recoverPassword(String email) {
    User user = userRepository
      .findByEmail(email)
      .orElseThrow(() ->
        new UserNotFoundException("Email não associado a nenhum usuário.")
      );

    String subject = "Recuperação de Senha";
    String body =
      "Recebemos sua solicitação de recuperação de senha. Sua nova senha é: [nova_senha]."; // Placeholder para nova senha

    Email recoveryEmail = new Email(email, subject, body);

    emailService.sendEmail(recoveryEmail);
  }
}
