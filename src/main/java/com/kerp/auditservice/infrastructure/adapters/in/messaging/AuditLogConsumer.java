package com.kerp.auditservice.infrastructure.adapters.in.messaging;

import com.kerp.auditservice.application.ports.in.RecordAuditLogUseCase;
import com.kerp.auditservice.domain.model.AuditEventPayload;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditLogConsumer {

    private final RecordAuditLogUseCase recordAuditLogUseCase;

    @PostConstruct
    public void init() {
        log.info("========== AUDIT CONSUMER BEAN CREATED ==========");
    }

    @KafkaListener(
            topics = "kerp.audit.event",
            groupId = "audit-service"
    )
    public void consume(AuditEventPayload payload) {
        recordAuditLogUseCase.record(payload);
    }
}
