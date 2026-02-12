package com.gustavo.user_service.controller;

import com.gustavo.user_service.dto.AuthDto;
import com.gustavo.user_service.dto.LoginResponseDto;
import com.gustavo.user_service.dto.RegisterDto;
import com.gustavo.user_service.infra.security.TokenService;
import com.gustavo.user_service.model.User;
import com.gustavo.user_service.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name= "authentication", description = "Endpoints for user authentication and registration")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;


    @PostMapping("/login")
    @Operation(summary = "Returns a JWT token upon successful authentication", description = "Checks user credentials and returns a JWT token if valid")
    @ApiResponse(responseCode = "200", description = "Successful authentication")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid AuthDto authDTO){
        log.info("Received login request for email: {}", authDTO.email());
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(
                    authDTO.email(),
                    authDTO.password()
            );
            var authentication = authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((User) authentication.getPrincipal());
            return ResponseEntity.ok(new LoginResponseDto(token));

        } catch (BadCredentialsException e) {
            log.error("Authentication failed for email: {}", authDTO.email(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/register")
    @Operation(summary = "Registers a new user", description = "Creates a new user account with the provided details")
    @ApiResponse(responseCode = "200", description = "Successful registration")
    @ApiResponse(responseCode = "400", description = "Email already in use")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterDto registerDTO){
        log.info("Received registration request for email: {}", registerDTO.email());
        if(this.userRepository.findByEmail(registerDTO.email()) != null ){
            log.warn("Registration failed: Email {} already in use", registerDTO.email());
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
