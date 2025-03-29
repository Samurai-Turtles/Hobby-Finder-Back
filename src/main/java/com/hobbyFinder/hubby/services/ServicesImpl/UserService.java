package com.hobbyFinder.hubby.services.ServicesImpl;

import java.util.Set;
import java.util.UUID;

import com.hobbyFinder.hubby.models.dto.email.EmailDto;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hobbyFinder.hubby.exception.TagInvalidaException;
import com.hobbyFinder.hubby.exception.NotFound.UserNotFoundException;
import com.hobbyFinder.hubby.models.dto.photos.PhotoDto;
import com.hobbyFinder.hubby.models.dto.user.UserDTO;
import com.hobbyFinder.hubby.models.dto.user.UserPutDTO;
import com.hobbyFinder.hubby.models.dto.user.UserResponseDTO;
import com.hobbyFinder.hubby.models.entities.Photo;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.models.enums.InterestEnum;
import com.hobbyFinder.hubby.repositories.UserRepository;
import com.hobbyFinder.hubby.services.IServices.UserInterface;
import com.hobbyFinder.hubby.services.Validation.UserValidator;
import com.hobbyFinder.hubby.util.GetUserLogged;

@Service
public class UserService implements UserInterface {

    private final UserRepository userRepository;

    @Lazy
    private final GetUserLogged userLogged;

    private final UserValidator userValidator;

    private final EmailService emailService;

    private final Set<InterestEnum> validInterests = Set.of(
            InterestEnum.values()
    );

    public UserService(UserRepository userRepository, GetUserLogged userLogged, UserValidator userValidator, EmailService emailService) {
        this.userRepository = userRepository;
        this.userLogged = userLogged;
        this.userValidator = userValidator;
        this.emailService = emailService;
    }

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
                .orElseThrow(() -> new UserNotFoundException("Email não associado a nenhum usuário."));

        String newPassword = UUID.randomUUID().toString();

        user.setPassword(newPassword);
        userRepository.save(user);

        sendPasswordRecoveryEmail(user.getEmail(), newPassword);
    }

    private void sendPasswordRecoveryEmail(String toEmail, String newPassword) {
        EmailDto emailDto = new EmailDto(toEmail, "Sua nova senha é: " + newPassword);

        emailService.enviaEmail(emailDto);
    }

}
