package com.kerp.auditservice.application.ports.out;

import com.kerp.auditservice.domain.model.AuditActivityByDay;
import com.kerp.auditservice.domain.model.AuditLog;
import com.kerp.auditservice.domain.model.AuditLogView;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuditLogRepositoryPort {
    void save(AuditLog auditLog);
    boolean existsByEventId(UUID eventId);   // idempotency guard against Kafka redelivery
    Optional<AuditLog> findByEventId(UUID eventId);
    Page<AuditLogView> search(String resourceType, String resourceId, String keyword, String status, int page);
    List<AuditActivityByDay> countActionsByUserOverLast30Days(String userId);
}
