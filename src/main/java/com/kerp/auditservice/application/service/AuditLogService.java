package com.kerp.auditservice.application.service;

import com.kerp.auditservice.application.ports.in.QueryAuditLogsUseCase;
import com.kerp.auditservice.application.ports.in.RecordAuditLogUseCase;
import com.kerp.auditservice.application.ports.out.AuditLogRepositoryPort;
import com.kerp.auditservice.domain.exception.AuditException;
import com.kerp.auditservice.domain.model.AuditActivityByDay;
import com.kerp.auditservice.domain.model.AuditEventPayload;
import com.kerp.auditservice.domain.model.AuditLog;
import com.kerp.auditservice.domain.model.AuditLogView;
import com.kerp.auditservice.infrastructure.mapper.AuditLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService implements QueryAuditLogsUseCase, RecordAuditLogUseCase {

    private final AuditLogRepositoryPort auditLogRepositoryPort;
    private final AuditLogMapper auditLogMapper;

    @Override
    public Page<AuditLogView> search(String resourceType, String resourceId, String keyword, String status, LocalDate date, int page) {
        return auditLogRepositoryPort.search(resourceType, resourceId, keyword, status, date, page);
    }

    @Override
    public List<AuditActivityByDay> countActionsByUserOverLast30Days(String userId) {
        return auditLogRepositoryPort.countActionsByUserOverLast30Days(userId);
    }

    @Override
    public void record(AuditEventPayload auditEventPayload) {
        if(auditLogRepositoryPort.existsByEventId(auditEventPayload.eventId())){
            throw AuditException
                    .alreadyExists("audit","eventId", String.valueOf(auditEventPayload.eventId()));
        }

        AuditLog auditLog = auditLogMapper.toDomain(auditEventPayload);
        auditLogRepositoryPort.save(auditLog);
    }
}
