package com.gustavo.user_service.service;

import com.gustavo.user_service.model.Role;
import com.gustavo.user_service.model.User;
import com.gustavo.user_service.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Should return UserDetails when user exists")
    void loadUserByUsername_ReturnsUserDetails_WhenUserExists() {
        String email = "email@test.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("hashed");
        user.setRole(Role.USER);
        when(userRepository.findByEmail(email)).thenReturn(user);

        UserDetails result = authService.loadUserByUsername(email);

        assertSame(user, result);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user does not exist")
    void loadUserByUsername_Throws_WhenUserNotFound() {
        String email = "email@test.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        UsernameNotFoundException ex = assertThrows(
                UsernameNotFoundException.class,
                () -> authService.loadUserByUsername(email)
        );
        assertEquals("User not found", ex.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }
}