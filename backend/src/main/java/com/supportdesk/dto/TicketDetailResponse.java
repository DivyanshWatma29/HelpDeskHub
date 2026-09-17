package com.supportdesk.dto;

import java.util.List;

public record TicketDetailResponse(
        TicketResponse ticket,
        List<AuditLogResponse> timeline
) {}
