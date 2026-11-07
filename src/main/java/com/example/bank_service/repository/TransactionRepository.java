package com.example.bank_service.repository;

import com.example.bank_service.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByIdempotencyKey(String idempotencyKey);

    @Query("""
        select t from Transaction t
        where t.fromAccountId in :accountIds
           or t.toAccountId   in :accountIds
        order by t.createdAt desc
        """)
    Page<Transaction> findHistoryByAccountIds(@Param("accountIds") List<Long> accountIds, Pageable pageable);
}