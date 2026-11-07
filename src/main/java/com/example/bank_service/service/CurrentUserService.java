package com.example.bank_service.service;

import com.example.bank_service.entity.User;
import com.example.bank_service.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserRepository users;

    public CurrentUserService(UserRepository users) {
        this.users = users;
    }

    public User require() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user");
        }

        String username = auth.getName();
        return users.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found: " + username));
    }
}
