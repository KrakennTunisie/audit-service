package com.kerp.auditservice.application.ports.in;

import com.kerp.auditservice.domain.model.AuditEventPayload;

public interface RecordAuditLogUseCase {
    void record(AuditEventPayload auditEventPayload);
}
