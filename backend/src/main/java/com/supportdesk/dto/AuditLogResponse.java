package com.supportdesk.dto;

import java.time.LocalDateTime;

public record AuditLogResponse(
        Long id,
        String action,
        String oldStatus,
        String newStatus,
        String note,
        String changedByName,
        LocalDateTime createdAt
) {}
