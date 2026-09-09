package com.pokearena.controller;

import com.pokearena.entity.Trainer;
import com.pokearena.model.dto.AuthResponse;
import com.pokearena.model.dto.LoginRequest;
import com.pokearena.model.dto.RegisterRequest;
import com.pokearena.repository.TrainerRepository;
import com.pokearena.security.CustomUserDetailsService;
import com.pokearena.security.JwtAuthenticationFilter;
import com.pokearena.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final TrainerRepository trainerRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthController(TrainerRepository trainerRepository, JwtService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.trainerRepository = trainerRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request){
        if (trainerRepository.findByName(request.name()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Trainer already exist");
        }

            Trainer trainer = new Trainer();
            trainer.setName(request.name());
            trainer.setEmail(request.email());
            trainer.setPassword(passwordEncoder.encode(request.password()));
            trainer.setRole("ROLE_TRAINER");

            trainerRepository.save(trainer);

            String token = jwtService.generateToken(trainer.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token, trainer.getName()));


        }



    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        // 1. Authenticate credentials (DaoAuthenticationProvider checks DB + BCrypt)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        // 2. If no exception thrown, credentials are correct! Generate and return token
        String token = jwtService.generateToken(request.username());
        return ResponseEntity.ok(new AuthResponse(token, request.username()));
    }

}
