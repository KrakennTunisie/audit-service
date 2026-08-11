package com.kerp.auditservice.infrastructure.adapters.out.persistance.entity;

import com.kerp.auditservice.domain.enums.AuditOutcome;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "audit_logs",
        indexes = {
                @Index(name = "idx_audit_resource", columnList = "resourceType,resourceId"),
                @Index(name = "idx_audit_actor", columnList = "actorUserId"),
                @Index(name = "idx_audit_correlation", columnList = "correlationId"),
                @Index(name = "idx_audit_timestamp", columnList = "timestamp")
        }
)
@Getter
@Setter
public class AuditLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID eventId;

    private UUID correlationId;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false)
    private String sourceService;

    @Column(nullable = false)
    private String actorUserId;

    private String actorFirstName;

    private String actorLastName;

    private String actorRole;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String resourceType;

    @Column(nullable = false)
    private String resourceId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String beforeStateJson;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String afterStateJson;

    @Enumerated(EnumType.STRING)
    private AuditOutcome outcome;

    private String failureReason;
    private String ipAddress;
    private Long enversRevision;

}
