package com.hobbyFinder.hubby.services.ServicesImpl;

import com.hobbyFinder.hubby.models.dto.user.UserDTO;
import com.hobbyFinder.hubby.models.entities.CustomPrincipal;
import com.hobbyFinder.hubby.repositories.UserRepository;
import com.hobbyFinder.hubby.services.IServices.UserInterface;
import com.hobbyFinder.hubby.util.GetUserLogged;
import com.hobbyFinder.hubby.models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService implements UserInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GetUserLogged userLogged;

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

}
