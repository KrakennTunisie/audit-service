package com.kerp.auditservice.infrastructure.adapters.out.persistance;

import java.time.LocalDate;

public interface AuditActivityByDayProjection {

    LocalDate getDate();

    Long getCount();
}
