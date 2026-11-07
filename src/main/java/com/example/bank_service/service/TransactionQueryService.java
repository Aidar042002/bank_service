package com.example.bank_service.service;

import com.example.bank_service.model.PageResponse;
import com.example.bank_service.model.TransactionResponse;
import com.example.bank_service.repository.AccountRepository;
import com.example.bank_service.repository.TransactionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionQueryService {

    private final AccountRepository accounts;
    private final TransactionRepository transactions;
    private final CurrentUserService currentUser;

    public TransactionQueryService(AccountRepository accounts,
                                   TransactionRepository transactions,
                                   CurrentUserService currentUser) {
        this.accounts = accounts;
        this.transactions = transactions;
        this.currentUser = currentUser;
    }

    @Transactional(readOnly = true)
    public PageResponse<TransactionResponse> history(int page, int size) {
        var user = currentUser.require();
        var accountIds = accounts.findAllByUserId(user.getId()).stream()
                .map(a -> a.getId())
                .toList();

        if (accountIds.isEmpty()) {
            return new PageResponse<>(List.of(), page, size, 0);
        }

        var pageable = PageRequest.of(page, Math.min(Math.max(size, 1), 100));
        var result = transactions.findHistoryByAccountIds(accountIds, pageable)
                .map(TransferService::toResponse);
        return PageResponse.of(result);
    }
}
