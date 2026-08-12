package com.kerp.auditservice.infrastructure.adapters.out.persistance.repository;

import com.kerp.auditservice.domain.enums.AuditOutcome;
import com.kerp.auditservice.domain.model.AuditLog;
import com.kerp.auditservice.infrastructure.adapters.out.persistance.AuditActivityByDayProjection;
import com.kerp.auditservice.infrastructure.adapters.out.persistance.entity.AuditLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLogEntity, UUID> {
    boolean existsByEventId(UUID eventId);

    Optional<AuditLog> findByEventId(UUID eventId);

    @Query("""
    SELECT a
    FROM AuditLogEntity a
    WHERE (:resourceType IS NULL OR a.resourceType = :resourceType)
      AND (:resourceId IS NULL OR a.resourceId = :resourceId)
      AND (:status IS NULL OR a.outcome = :status)
      AND (
            :keyword IS NULL OR :keyword = ''
            OR LOWER(a.action) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(a.actorFirstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(a.actorLastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
    ORDER BY a.timestamp DESC
    """)
    Page<AuditLogEntity> search(
            @Param("resourceType") String resourceType,
            @Param("resourceId") String resourceId,
            @Param("keyword") String keyword,
            @Param("status") AuditOutcome status,
            Pageable pageable
    );

    @Query("""
SELECT a
FROM AuditLogEntity a
WHERE (:resourceType IS NULL OR a.resourceType = :resourceType)
  AND (:resourceId IS NULL OR a.resourceId = :resourceId)
  AND (:status IS NULL OR a.outcome = :status)
  AND a.timestamp >= :startOfDay
  AND a.timestamp < :startOfNextDay
  AND (
        :keyword IS NULL OR :keyword = ''
        OR LOWER(a.action) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(a.actorFirstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(a.actorLastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
ORDER BY a.timestamp DESC
""")
    Page<AuditLogEntity> searchByDate(
            @Param("resourceType") String resourceType,
            @Param("resourceId") String resourceId,
            @Param("keyword") String keyword,
            @Param("status") AuditOutcome status,
            @Param("startOfDay") Instant startOfDay,
            @Param("startOfNextDay") Instant startOfNextDay,
            Pageable pageable
    );

    @Query(value = """
    SELECT
        days.day::date AS date,
        COUNT(al.id) AS count
    FROM generate_series(
        CURRENT_DATE - INTERVAL '29 days',
        CURRENT_DATE,
        INTERVAL '1 day'
    ) AS days(day)
    LEFT JOIN audit_logs al
        ON al.actor_user_id = :userId
        AND al.timestamp >= days.day
        AND al.timestamp < days.day + INTERVAL '1 day'
    GROUP BY days.day
    ORDER BY days.day ASC
    """, nativeQuery = true)
    List<AuditActivityByDayProjection> countActionsByUserOverLast30Days(
            @Param("userId") String userId
    );

}
