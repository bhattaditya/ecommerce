package com.example.ecommerce.security;

import com.example.ecommerce.dto.*;
import com.example.ecommerce.entity.Role;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.exception.DuplicateEmailException;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already registered: " + request.email());
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password())); // BCrypt hash
        user.setRole(Role.valueOf(request.role().toUpperCase())); // Convert "customer" to Role.CUSTOMER

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), String.valueOf(user.getRole()));
        return new AuthResponse(token, user.getEmail(), user.getRole().toString());
    }

    public AuthResponse login(LoginRequest request) {
        // AuthenticationManager verifies email + password against DB
        // Throws BadCredentialsException if wrong — Spring Security handles this
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(), request.password()));

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().toString());
        return new AuthResponse(token, user.getEmail(), user.getRole().toString());
    }
}