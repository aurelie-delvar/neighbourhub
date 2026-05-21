package com.neighborrhub.api.controller;

import com.neighborrhub.api.dto.LoginDto;
import com.neighborrhub.api.dto.RegisterDto;
import com.neighborrhub.api.entity.Role;
import com.neighborrhub.api.entity.User;
import com.neighborrhub.api.exception.BusinessException;
import com.neighborrhub.api.repositories.NeighbourhoodRepository;
import com.neighborrhub.api.repositories.RoleRepository;
import com.neighborrhub.api.repositories.UserRepository;
import com.neighborrhub.api.service.JwtService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final NeighbourhoodRepository neighbourhoodRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository,
            RoleRepository roleRepository,
            NeighbourhoodRepository neighbourhoodRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.neighbourhoodRepository = neighbourhoodRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto dto) {
        if (userRepository.findByMail(dto.getMail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email déjà utilisé");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setMail(dto.getMail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        // Assigne le rôle USER par défaut
        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new BusinessException("Rôle introuvable", HttpStatus.NOT_FOUND));
        user.setRoles(Set.of(role));

        // Assigne le quartier si fourni
        if (dto.getNeighbourhoodId() != null) {
            neighbourhoodRepository.findById(dto.getNeighbourhoodId())
                    .ifPresent(user::setNeighbourhood);
        }

        userRepository.save(user);
        return ResponseEntity.status(201).body("Compte créé avec succès");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getMail(), dto.getPassword()));

        String token = jwtService.generateToken(dto.getMail());
        return ResponseEntity.ok(Map.of("token", token));
    }
}