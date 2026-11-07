package com.example.bank_service.model;

public record LoginResponse(
        String accessToken,
        String tokenType
) {}