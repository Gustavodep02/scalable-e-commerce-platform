package com.gustavo.user_service.controller;

import com.gustavo.user_service.dto.UserDto;
import com.gustavo.user_service.model.User;
import com.gustavo.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.TableGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "users", description = "Endpoints for managing users")
public class UserController {

    private final UserService userService;

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a user by ID", description = "Deletes the user with the specified ID from the system")
    @ApiResponse(responseCode = "204", description = "User successfully deleted")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        log.info("Received request to delete user with id: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    @Operation(summary = "Retrieves all users", description = "Returns a list of all users in the system")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Received request to get all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieves a user by ID", description = "Returns the user with the specified ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        log.info("Received request to get user with id: {}", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PatchMapping
    @Operation(summary = "Updates a user", description = "Updates the details of an existing user")
    @ApiResponse(responseCode = "200", description = "Successfully updated user")
    public ResponseEntity<User> updateUser(@RequestBody UserDto userDto) {
        log.info("Received request to update user with id: {}", userDto.id());
        return ResponseEntity.ok(userService.updateUser(userDto));
    }
}
