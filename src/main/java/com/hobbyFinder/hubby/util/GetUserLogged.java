package com.hobbyFinder.hubby.util;

import java.beans.JavaBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.hobbyFinder.hubby.models.entities.CustomPrincipal;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.repositories.UserRepository;

@Component
public class GetUserLogged {

    @Autowired
    private UserRepository userRepository;
    
    public User getUserLogged() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal customPrincipal = (CustomPrincipal) auth.getPrincipal();
        
        return userRepository.findByUsername(customPrincipal.username());
    }

}
