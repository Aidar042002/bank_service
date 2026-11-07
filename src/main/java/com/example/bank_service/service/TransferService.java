package com.example.bank_service.service;

import com.example.bank_service.entity.Account;
import com.example.bank_service.entity.Transaction;
import com.example.bank_service.entity.enums.StatusEnum;
import com.example.bank_service.exception.BusinessException;
import com.example.bank_service.model.TransactionResponse;
import com.example.bank_service.repository.AccountRepository;
import com.example.bank_service.repository.TransactionRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class TransferService {

    private final AccountRepository accounts;
    private final TransactionRepository transactions;
    private final CurrentUserService currentUser;
    private final AuditService audit;

    public TransferService(
            AccountRepository accounts,
            TransactionRepository transactions,
            CurrentUserService currentUser,
            AuditService audit
    ) {
        this.accounts = accounts;
        this.transactions = transactions;
        this.currentUser = currentUser;
        this.audit = audit;
    }

    @Transactional
    public TransactionResponse transfer(
            Long fromAccountId,
            Long toAccountId,
            BigDecimal amount,
            String idempotencyKey
    ) {

        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            var existing = transactions.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) {
                return toResponse(existing.get());
            }
        }

        var user = currentUser.require();
        Account from = accounts.findByIdForUpdate(fromAccountId)
                .orElseThrow(() -> new BusinessException("ACCOUNT_NOT_FOUND", "Аккаунт не найден"));

        if (from.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("INSUFFICIENT_FUNDS", "Отрицательный баланс");
        }

        Account to = accounts.findByIdForUpdate(toAccountId)
                .orElseThrow(() -> new BusinessException("ACCOUNT_NOT_FOUND", "Аккаунт отрпавителя не найден"));

        if (from.getId().equals(to.getId())) {
            throw new BusinessException("SAME_ACCOUNT", "Нельзя отправить на тот же аккаунт");
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        var tx = new Transaction();
        tx.setFromAccountId(from.getId());
        tx.setToAccountId(to.getId());
        tx.setAmount(amount);
        tx.setStatus(StatusEnum.COMPLETED);
        tx.setIdempotencyKey(idempotencyKey);

        try {
            transactions.saveAndFlush(tx);
        } catch (DataIntegrityViolationException e) {
            return transactions.findByIdempotencyKey(idempotencyKey)
                    .map(TransferService::toResponse)
                    .orElseThrow(() -> e);
        }

        audit.log(user.getId(), "TRANSFER",
                String.format(
                        "{\"transactionId\":%d,\"fromAccountId\":%d,\"toAccountId\":%d,\"amount\":\"%s\",\"currency\":\"%s\"}",
                        tx.getId(),
                        from.getId(),
                        to.getId(),
                        amount.toPlainString(),
                        from.getCurrency()));

        return toResponse(tx);
    }

    static TransactionResponse toResponse(Transaction t) {
        return new TransactionResponse(
                t.getId(),
                t.getFromAccountId(),
                t.getToAccountId(),
                t.getAmount(),
                t.getStatus().name(),
                t.getCreatedAt()
        );
    }
}