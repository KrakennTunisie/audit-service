package com.kerp.auditservice.infrastructure.adapters.out.persistance;

import com.kerp.auditservice.application.ports.out.AuditLogRepositoryPort;
import com.kerp.auditservice.domain.enums.AuditOutcome;
import com.kerp.auditservice.domain.exception.AuditException;
import com.kerp.auditservice.domain.model.AuditActivityByDay;
import com.kerp.auditservice.domain.model.AuditLog;
import com.kerp.auditservice.domain.model.AuditLogView;
import com.kerp.auditservice.infrastructure.adapters.out.persistance.entity.AuditLogEntity;
import com.kerp.auditservice.infrastructure.adapters.out.persistance.repository.AuditLogRepository;
import com.kerp.auditservice.infrastructure.mapper.AuditLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuditLogRepositoryAdapter implements AuditLogRepositoryPort {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    @Override
    public void save(AuditLog auditLog) {
        try{
            AuditLogEntity auditLogEntity = auditLogMapper.modelToEntity(auditLog);
            log.info("after: {}", auditLogEntity.getAfterStateJson());
            auditLogRepository.save(auditLogEntity);
        } catch (Exception e) {
            throw AuditException.badRequest(e.getMessage());
        }
    }

    @Override
    public boolean existsByEventId(UUID eventId) {
        return auditLogRepository.existsByEventId(eventId);
    }

    @Override
    public Optional<AuditLog> findByEventId(UUID eventId) {
        return Optional.of(auditLogRepository.findByEventId(eventId)
                .orElseThrow(() ->
                        AuditException.notFound("auditLog", "eventId", String.valueOf(eventId))));
    }

    @Override
    public Page<AuditLogView> search(String resourceType, String resourceId, String keyword, String status, LocalDate date, int page) {
        Pageable pageable = PageRequest.of(
                Math.max(page - 1, 0),
                5
        );

        AuditOutcome outcome = null;

        if (status != null && !status.isBlank()) {
            outcome = AuditOutcome.valueOf(status.toUpperCase());
        }
        Page<AuditLogEntity> logs;

        if (date != null) {

            Instant startOfDay = date
                    .atStartOfDay(ZoneOffset.UTC)
                    .toInstant();

            Instant startOfNextDay = date
                    .plusDays(1)
                    .atStartOfDay(ZoneOffset.UTC)
                    .toInstant();

            logs = auditLogRepository.searchByDate(
                    resourceType,
                    resourceId,
                    keyword,
                    outcome,
                    startOfDay,
                    startOfNextDay,
                    pageable
            );

        } else {

            logs = auditLogRepository.search(
                    resourceType,
                    resourceId,
                    keyword,
                    outcome,
                    pageable
            );
        }

        return logs.map(auditLogMapper::toView);
    }

    @Override
    public List<AuditActivityByDay> countActionsByUserOverLast30Days(String userId) {
        return auditLogRepository.countActionsByUserOverLast30Days(userId)
                .stream()
                .map(item -> new AuditActivityByDay(
                        item.getDate(),
                        item.getCount()
                ))
                .toList();
    }


}
