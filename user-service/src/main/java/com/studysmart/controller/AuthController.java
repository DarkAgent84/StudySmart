package com.studysmart.controller;

import com.studysmart.dto.AuthRequest;
import com.studysmart.dto.AuthResponse;
import com.studysmart.entity.User;
import com.studysmart.repository.UserRepository;
import com.studysmart.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElse(null);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new AuthResponse(null, "User not found"));
            }

            // Plain text password comparison
            if (!request.getPassword().equals(user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new AuthResponse(null, "Invalid credentials"));
            }

            String token = jwtUtil.generateToken(user.getEmail());
            return ResponseEntity.ok(new AuthResponse(token, "Authentication successful"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse(null, "Login error: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        try {
            // Check if user already exists
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body(
                        new AuthResponse(null, "Email already registered"));
            }

            // Create new user with plain text password
            User newUser = User.builder()
                    .email(request.getEmail())
                    .password(request.getPassword())
                    .role("USER")
                    .build();

            userRepository.save(newUser);

            // Generate token for the new user
            String token = jwtUtil.generateToken(newUser.getEmail());
            return ResponseEntity.ok(new AuthResponse(token, "Registration successful"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse(null, "Registration error: " + e.getMessage()));
        }
    }
}