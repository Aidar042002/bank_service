package com.example.bank_service.service;

import com.example.bank_service.entity.User;
import com.example.bank_service.jwt.JwtService;
import com.example.bank_service.model.LoginRequest;
import com.example.bank_service.model.LoginResponse;
import com.example.bank_service.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository, UserRepository userRepository1,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userRepository = userRepository1;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }
    public Map<String, Object> register(LoginRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            return Map.of("code", "USER_ALREADY_EXISTS",
                    "message", "Имя пользователя уже занято");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        return Map.of("id", user.getId(), "username", user.getUsername());
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authRequest = UsernamePasswordAuthenticationToken.unauthenticated(
                request.username(), request.password()
        );
        Authentication authResponse = authenticationManager.authenticate(authRequest);

        String token = jwtService.generateToken(authResponse);

        return new LoginResponse(token, "Bearer");
    }
}
