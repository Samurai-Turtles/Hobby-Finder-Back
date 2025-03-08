package com.hobbyFinder.hubby.services.ServicesImpl;

import com.hobbyFinder.hubby.models.dto.user.UserDTO;
import com.hobbyFinder.hubby.models.entities.CustomPrincipal;
import com.hobbyFinder.hubby.models.dto.user.UserPutDTO;
import com.hobbyFinder.hubby.models.entities.CustomPrincipal;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.models.enums.InterestEnum;
import com.hobbyFinder.hubby.repositories.UserRepository;
import com.hobbyFinder.hubby.services.IServices.UserInterface;
import com.hobbyFinder.hubby.util.GetUserLogged;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.services.Validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class UserService implements UserInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GetUserLogged userLogged;

    @Autowired
    private UserValidator userValidator;

    private final Set<InterestEnum> validInterests = Set.of(InterestEnum.values());


    @Override
    public UserDTO getUser(UUID uuid) {
        return userRepository.findById(uuid)
                .map(user -> new UserDTO(user.getEmail(), user.getUsername(), user.getRole()))
                .orElseThrow(() -> new UsernameNotFoundException("User não encontrado."));
    }

    @Override
    public void deleteUser() {
        User user = userLogged.getUserLogged();
        userRepository.delete(user);
        userRepository.flush();
    }



    public UserDTO updateUser(UserPutDTO request) throws HubbyException {
        User user = getUserLogged();

        if (request.username() != null){
            userValidator.validaUsername(request.username());
            user.setUsername(request.username());
        }

        if (request.email() != null){
            userValidator.validaEmail(request.email());
            user.setEmail(request.email());
        }

        if (request.password() != null) {
            userValidator.validaPassword(request.password());
            user.setPassword(new BCryptPasswordEncoder().encode(request.password()));
        }

        if (request.interests() != null) {
            if (!validInterests.containsAll(request.interests()))
                throw new TagInvalidaException("Tag invalida");
            user.setInterests(request.interests());
        }

        if (request.bio() != null){
            user.setBio(request.bio());
        }

        if (request.name() != null){
            user.setNome(request.name());
        }

        userRepository.save(user);
        return new UserDTO(user.getEmail(), user.getUsername(), user.getRole());
    }

    private User getUserLogged() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal customPrincipal = (CustomPrincipal) auth.getPrincipal();
        return userRepository.findByUsername(customPrincipal.username());
    }
}
