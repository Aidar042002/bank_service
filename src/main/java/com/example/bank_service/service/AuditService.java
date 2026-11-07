package com.example.bank_service.service;

import com.example.bank_service.entity.AuditLog;
import com.example.bank_service.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    private final AuditLogRepository repository;

    public AuditService(AuditLogRepository repository) {
        this.repository = repository;
    }

    public void log(Long userId, String action, String details) {
        var log = new AuditLog();
        log.setUserId(userId);
        log.setAction(action);
        log.setDetails(details);

        repository.save(log);
    }
}
