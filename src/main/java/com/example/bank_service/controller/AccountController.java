package com.example.bank_service.controller;

import com.example.bank_service.model.AccountResponse;
import com.example.bank_service.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> create() {
        return ResponseEntity.status(201).body(service.create());
    }

    @GetMapping
    public List<AccountResponse> list() {
        return service.listMine();
    }
}
