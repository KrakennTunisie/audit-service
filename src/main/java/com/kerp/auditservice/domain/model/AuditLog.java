package com.kerp.auditservice.domain.model;

import com.kerp.auditservice.domain.enums.AuditOutcome;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Builder
@Getter
@Setter
public class AuditLog {
    private UUID id;
    private UUID eventId;          // idempotency key, from the original outbox event
    private UUID correlationId;
    private Instant timestamp;
    private String sourceService;  // "billing-service", "iam-service"
    private Actor actor;
    private String action;         // "INVOICE_UPDATED", "USER_ROLE_REVOKED"
    private String resourceType;   // "Invoice", "User"
    private String resourceId;
    private Map<String, Object> beforeState;
    private Map<String, Object> afterState;
    private AuditOutcome outcome;
    private String failureReason;
    private String ipAddress;
    private Long enversRevision;   // nullable, links to Envers history in source service

}
