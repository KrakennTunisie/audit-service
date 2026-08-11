package com.kerp.auditservice.infrastructure.adapters.in.web;

import com.kerp.auditservice.application.ports.in.QueryAuditLogsUseCase;
import com.kerp.auditservice.domain.model.AuditActivityByDay;
import com.kerp.auditservice.domain.model.AuditLogView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final QueryAuditLogsUseCase auditLogsUseCase;

    @GetMapping
    public ResponseEntity<Page<AuditLogView>> getAll(
            @RequestParam(required = false) String resourceType,
            @RequestParam(required = false) String resourceId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(
                auditLogsUseCase.search(
                        resourceType,
                        resourceId,
                        keyword,
                        status,
                        page
                )
        );
    }

    @GetMapping("/activity")
    public ResponseEntity<List<AuditActivityByDay>> getActivity(
            @RequestParam String userId
    ) {
        return ResponseEntity.ok(
                auditLogsUseCase.countActionsByUserOverLast30Days(userId)
        );
    }
}
