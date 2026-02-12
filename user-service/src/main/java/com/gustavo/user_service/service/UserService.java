package com.gustavo.user_service.service;

import com.gustavo.user_service.dto.UserDto;
import com.gustavo.user_service.model.User;
import com.gustavo.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User updateUser(UserDto userDto) {
        User user = userRepository.findUserById(userDto.id())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(userDto.email() != null && userRepository.existsByEmailAndIdNot(userDto.email(), userDto.id())){
            log.error("Email {} is already in use by another user", userDto.email());
            throw new RuntimeException("Email already in use");
        }
        if(userDto.email() != null){
                user.setEmail(userDto.email());
        }

        if(userDto.name() != null){
            user.setName(userDto.name());
        }

        if(userDto.password() != null){
            String hashedPassword = passwordEncoder.encode(userDto.password());
            user.setPassword(hashedPassword);
        }

        return userRepository.save(user);
    }

    public void deleteUser(UUID userId) {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    public User getUserById(UUID userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
