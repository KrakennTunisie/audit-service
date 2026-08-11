package com.kerp.auditservice.infrastructure.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kerp.auditservice.domain.enums.AuditOutcome;
import com.kerp.auditservice.domain.model.Actor;
import com.kerp.auditservice.domain.model.AuditEventPayload;
import com.kerp.auditservice.domain.model.AuditLog;
import com.kerp.auditservice.domain.model.AuditLogView;
import com.kerp.auditservice.infrastructure.adapters.out.persistance.entity.AuditLogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuditLogMapper {

    private final ObjectMapper objectMapper;

    public AuditLogEntity toEntity(AuditEventPayload payload) {
        AuditLogEntity entity = new AuditLogEntity();

        entity.setEventId(payload.eventId());
        entity.setCorrelationId(payload.correlationId());
        entity.setTimestamp(payload.timestamp());
        entity.setSourceService(payload.sourceService());
        entity.setActorUserId(payload.actorUserId());
        entity.setActorFirstName(payload.actorFirstName());
        entity.setActorLastName(payload.actorLastName());
        entity.setActorRole(payload.actorRoles().get(payload.actorRoles().size()));
        entity.setAction(payload.action());
        entity.setResourceType(payload.resourceType());
        entity.setResourceId(payload.resourceId());
        entity.setBeforeStateJson(toJson(payload.beforeState()));
        entity.setAfterStateJson(toJson(payload.afterState()));
        entity.setOutcome(
                payload.outcome() != null
                        ? AuditOutcome.valueOf(payload.outcome())
                        : null
        );
        entity.setFailureReason(payload.failureReason());
        entity.setIpAddress(payload.ipAddress());
        entity.setEnversRevision(payload.enversRevision());

        return entity;
    }

    public AuditLogEntity modelToEntity(AuditLog auditLog) {
        AuditLogEntity entity = new AuditLogEntity();

        entity.setId(auditLog.getId());
        entity.setEventId(auditLog.getEventId());
        entity.setCorrelationId(auditLog.getCorrelationId());
        entity.setTimestamp(auditLog.getTimestamp());
        entity.setSourceService(auditLog.getSourceService());

        if (auditLog.getActor() != null) {
            entity.setActorUserId(auditLog.getActor().userId());
            entity.setActorFirstName(auditLog.getActor().firstName());
            entity.setActorLastName(auditLog.getActor().lastName());
            entity.setActorRole(auditLog.getActor().roles().get(auditLog.getActor().roles().size()-1));
        }

        entity.setAction(auditLog.getAction());
        entity.setResourceType(auditLog.getResourceType());
        entity.setResourceId(auditLog.getResourceId());

        entity.setBeforeStateJson(toJson(auditLog.getBeforeState()));
        entity.setAfterStateJson(toJson(auditLog.getAfterState()));

        entity.setOutcome(auditLog.getOutcome());
        entity.setFailureReason(auditLog.getFailureReason());
        entity.setIpAddress(auditLog.getIpAddress());
        entity.setEnversRevision(auditLog.getEnversRevision());

        return entity;
    }


    public AuditLog toDomain(AuditEventPayload payload) {
        return AuditLog.builder()
                .eventId(payload.eventId())
                .correlationId(payload.correlationId())
                .timestamp(payload.timestamp())
                .sourceService(payload.sourceService())
                .actor(
                        new Actor(payload.actorUserId(),
                                payload.actorFirstName(),
                                payload.actorLastName(),
                                payload.actorRoles())
                )
                .action(payload.action())
                .resourceType(payload.resourceType())
                .resourceId(payload.resourceId())
                .beforeState(payload.beforeState())
                .afterState(payload.afterState())
                .outcome(
                        payload.outcome() != null
                                ? AuditOutcome.valueOf(payload.outcome())
                                : null
                )
                .failureReason(payload.failureReason())
                .ipAddress(payload.ipAddress())
                .enversRevision(payload.enversRevision())
                .build();
    }

    public AuditLogView toView(AuditLogEntity entity) {
        Actor actor = new Actor(
                entity.getActorUserId(),
                entity.getActorFirstName(),
                entity.getActorLastName(),
                List.of(entity.getActorRole())
        );

        return new AuditLogView(
                entity.getId(),
                entity.getCorrelationId(),
                entity.getTimestamp(),
                entity.getSourceService(),
                actor,
                entity.getAction(),
                entity.getResourceType(),
                entity.getResourceId(),
                fromJson(entity.getBeforeStateJson()),
                fromJson(entity.getAfterStateJson()),
                entity.getOutcome() != null
                        ? entity.getOutcome().name()
                        : null,
                entity.getFailureReason(),
                entity.getEnversRevision()
        );
    }
    private String toJson(Object value) {
        if (value == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize audit state", e);
        }
    }

    private Map<String, Object> fromJson(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return objectMapper.readValue(
                    value,
                    new TypeReference<Map<String, Object>>() {}
            );
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(
                    "Failed to deserialize audit state",
                    e
            );
        }
    }
}
