package com.gustavo.user_service.service;

import com.gustavo.user_service.dto.UserDto;
import com.gustavo.user_service.model.User;
import com.gustavo.user_service.repository.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should update user information successfully")
    void updateExistingUser() {
        var user = new User();
        var userDto = new com.gustavo.user_service.dto.UserDto(
                UUID.randomUUID(),
                "New Name",
                "email@test.com",
                "newpassword"
        );
        user.setId(userDto.id());
        user.setEmail("old@email.com");
        user.setName("Old Name");
        user.setPassword("old-hash");
        when(userRepository.existsByEmailAndIdNot(userDto.email(), userDto.id())).thenReturn(false);
        when(userRepository.findUserById(userDto.id())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        var updatedUser = userService.updateUser(userDto);
        assertEquals("New Name", updatedUser.getName());
        assertEquals("email@test.com", updatedUser.getEmail());

    }

    @Test
    @DisplayName("Should throw exception when updating user with email already in use")
    void updateUserEmailAlreadyInUse() {
        var user = new User();
        var userDto = new UserDto(
                UUID.randomUUID(),
                "New Name",
                "email@test.com",
                "newpassword"
        );
        user.setId(userDto.id());
        user.setEmail("old@email.com");
        user.setName("Old Name");
        user.setPassword("old-hash");
        when(userRepository.existsByEmailAndIdNot(userDto.email(), userDto.id())).thenReturn(true);
        when(userRepository.findUserById(userDto.id())).thenReturn(Optional.of(user));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(userDto);
        });
        assertEquals("Email already in use", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when updating non-existing user")
    void updateUserNotFound() {
        var userDto = new UserDto(
                UUID.randomUUID(),
                "New Name",
                "email@test.com",
                "newpassword"
        );

        when(userRepository.findUserById(userDto.id())).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(userDto);
        });
        assertEquals("User not found", exception.getMessage());
        }

    @Test
    @DisplayName("Should retrieve user by ID successfully")
    void getExistingUserById() {
        var userId = UUID.randomUUID();
        var user = new User();
        user.setId(userId);
        user.setEmail("old@email.com");
        user.setName("Old Name");
        user.setPassword("old-hash");
        when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));
        var retrievedUser = userService.getUserById(userId);
        assertNotNull(retrievedUser);
    }

    @Test
    @DisplayName("Should throw exception when retrieving non-existing user by ID")
    void getUserByIdNotFound() {
        var userId = UUID.randomUUID();
        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById(userId);
        });
        assertEquals("User not found", exception.getMessage());
    }
}