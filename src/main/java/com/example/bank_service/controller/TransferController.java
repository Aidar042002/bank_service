package com.example.bank_service.controller;

import com.example.bank_service.model.TransactionResponse;
import com.example.bank_service.model.TransferRequest;
import com.example.bank_service.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService service;

    public TransferController(TransferService service) {
        this.service = service;
    }

    @PostMapping
    public TransactionResponse transfer(
            @Valid @RequestBody TransferRequest req,
            @RequestHeader(value = "Idempotency-Key", required = false)
            String idempotencyKey
    ) {
        return service.transfer(req.fromAccountId(), req.toAccountId(), req.amount(), idempotencyKey);
    }
}
