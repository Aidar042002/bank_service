package com.example.bank_service.model;

import java.math.BigDecimal;

public record AccountResponse(
        Long id,
        BigDecimal balance,
        String currency
) {}
