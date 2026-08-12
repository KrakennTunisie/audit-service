package com.kerp.auditservice.application.ports.in;

import com.kerp.auditservice.domain.model.AuditActivityByDay;
import com.kerp.auditservice.domain.model.AuditLogView;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public interface QueryAuditLogsUseCase {
    Page<AuditLogView> search(String resourceType, String resourceId, String keyword , String status , LocalDate date,  int page);
    List<AuditActivityByDay> countActionsByUserOverLast30Days(String userId);
}
