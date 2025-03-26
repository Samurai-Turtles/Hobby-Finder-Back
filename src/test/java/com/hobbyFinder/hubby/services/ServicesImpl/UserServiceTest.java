package com.hobbyFinder.hubby.services.ServicesImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hobbyFinder.hubby.exception.NotFound.UserNotFoundException;
import com.hobbyFinder.hubby.models.entities.Email;
import com.hobbyFinder.hubby.models.entities.User;
import com.hobbyFinder.hubby.repositories.UserRepository;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRecoverPassword_UserExists() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        userService.recoverPassword(email);

        verify(emailService, times(1)).sendEmail(any(Email.class));
    }

    @Test
    public void testRecoverPassword_UserDoesNotExist() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.recoverPassword(email);
        });
    }
}
