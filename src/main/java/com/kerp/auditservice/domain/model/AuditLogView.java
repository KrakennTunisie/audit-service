package com.kerp.auditservice.domain.model;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record AuditLogView(
        UUID id,
        UUID correlationId,
        Instant timestamp,
        String sourceService,
        Actor actor,
        String action,
        String resourceType,
        String resourceId,
        Map<String, Object> before,
        Map<String, Object> after,
        String outcome,
        String failureReason,
        Long enversRevision
) {

}
