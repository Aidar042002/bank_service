package com.example.bank_service.model;

public record LoginRequest(
        String username,
        String password
) {}