package com.example.bank_service.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequest(
        @NotNull
        Long fromAccountId,
        @NotNull
        Long toAccountId,
        @NotNull @DecimalMin(value = "0.01", message = "Сумма должна быть больше 0")
        BigDecimal amount
) {}
