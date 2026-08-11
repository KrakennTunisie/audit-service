package com.kerp.auditservice.domain.model;

import java.time.LocalDate;

public record AuditActivityByDay(
        LocalDate date,
        Long count
) {
}
