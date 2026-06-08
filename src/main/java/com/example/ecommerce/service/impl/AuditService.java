package com.example.ecommerce.service.impl;

import com.example.ecommerce.entity.AuditLog;
import com.example.ecommerce.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAction(
            String action,
            String performedBy,
            String details) {

        AuditLog audit = new AuditLog();
        audit.setAction(action);
        audit.setPerformedBy(performedBy);
        audit.setDetails(details);

        auditLogRepository.save(audit);
    }
}