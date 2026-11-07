package com.example.bank_service.controller;

import com.example.bank_service.model.PageResponse;
import com.example.bank_service.model.TransactionResponse;
import com.example.bank_service.service.TransactionQueryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionQueryService service;

    public TransactionController(TransactionQueryService service) {
        this.service = service;
    }

    @GetMapping
    public PageResponse<TransactionResponse> history(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return service.history(page, size);
    }
}
