package com.gustavo.user_service.controller;

import com.gustavo.user_service.dto.AuthDto;
import com.gustavo.user_service.dto.LoginResponseDto;
import com.gustavo.user_service.dto.RegisterDto;
import com.gustavo.user_service.infra.security.TokenService;
import com.gustavo.user_service.model.User;
import com.gustavo.user_service.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid AuthDto authDTO){
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(
                    authDTO.email(),
                    authDTO.password()
            );
            var authentication = authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((User) authentication.getPrincipal());
            return ResponseEntity.ok(new LoginResponseDto(token));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterDto registerDTO){
        System.out.println("peido molhado");
        if(this.userRepository.findByEmail(registerDTO.email()) != null ){
            return ResponseEntity.badRequest().body("Email already in use");
        }

        String encodedPassword = passwordEncoder.encode(registerDTO.password());

        User user = new User(
                registerDTO.email(),
                encodedPassword,
                registerDTO.name()
        );
        this.userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}
