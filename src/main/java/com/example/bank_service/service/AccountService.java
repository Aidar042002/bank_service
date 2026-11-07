package com.example.bank_service.service;

import com.example.bank_service.entity.Account;
import com.example.bank_service.model.AccountResponse;
import com.example.bank_service.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class AccountService {

    private final AccountRepository accounts;
    private final CurrentUserService currentUser;
    private final AuditService audit;

    public AccountService(AccountRepository accounts,
                          CurrentUserService currentUser,
                          AuditService audit) {
        this.accounts = accounts;
        this.currentUser = currentUser;
        this.audit = audit;
    }

    @Transactional
    public AccountResponse create() {
        var user = currentUser.require();

        var account = new Account();
        account.setUserId(user.getId());
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency("RUB");
        accounts.save(account);

        audit.log(
                user.getId(),
                "ACCOUNT_CREATED",
                String.format("{\"accountId\":%d,\"currency\":\"%s\"}",
                    account.getId(),
                    account.getCurrency()
                ));

        return toResponse(account);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> listMine() {
        var user = currentUser.require();
        return accounts.findAllByUserId(user.getId()).stream()
                .map(AccountService::toResponse)
                .toList();
    }

    static AccountResponse toResponse(Account a) {
        return new AccountResponse(a.getId(), a.getBalance(), a.getCurrency());
    }
}
