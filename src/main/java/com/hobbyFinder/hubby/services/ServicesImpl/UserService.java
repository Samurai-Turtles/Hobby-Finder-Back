package com.hobbyFinder.hubby.services.ServicesImpl;

import com.hobbyFinder.hubby.models.dto.user.UserDTO;
import com.hobbyFinder.hubby.repositories.UserRepository;
import com.hobbyFinder.hubby.services.IServices.UserInterface;
import com.hobbyFinder.hubby.models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService implements UserInterface {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO getUser(UUID uuid) {
        return userRepository.findById(uuid)
                .map(user -> new UserDTO(user.getEmail(), user.getUsername(), user.getRole()))
                .orElseThrow(() -> new UsernameNotFoundException("User não encontrado."));
    }

    @Override
    public void deleteUser() {
        User user = getUserLogged();
        userRepository.delete(user);
        userRepository.flush();
    }

    // utilize essa função para quando quiser pegar um usuário ja logado
    private User getUserLogged() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(name);
    }
}
