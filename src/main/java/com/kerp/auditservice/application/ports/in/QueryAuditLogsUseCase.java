package com.kerp.auditservice.application.ports.in;

import com.kerp.auditservice.domain.model.AuditActivityByDay;
import com.kerp.auditservice.domain.model.AuditLogView;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QueryAuditLogsUseCase {
    Page<AuditLogView> search(String resourceType, String resourceId, String keyword , String status , int page);
    List<AuditActivityByDay> countActionsByUserOverLast30Days(String userId);
}
