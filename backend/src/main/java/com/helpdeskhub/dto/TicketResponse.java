package com.helpdeskhub.dto;

import com.helpdeskhub.model.Priority;
import com.helpdeskhub.model.TicketStatus;
import java.time.LocalDateTime;

public record TicketResponse(
        Long id,
        String ticketNumber,
        String title,
        String description,
        Priority priority,
        TicketStatus status,
        Long departmentId,
        String departmentName,
        String departmentCode,
        String requesterName,
        String requesterEmail,
        String assignedToName,
        String adminNote,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
