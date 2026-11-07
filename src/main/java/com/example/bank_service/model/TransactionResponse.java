package com.example.bank_service.model;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponse(
        Long id,
        Long fromAccountId,
        Long toAccountId,
        BigDecimal amount,
        String status,
        Instant createdAt
) {}
