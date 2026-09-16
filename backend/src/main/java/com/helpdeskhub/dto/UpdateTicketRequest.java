package com.helpdeskhub.dto;

import com.helpdeskhub.model.TicketStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateTicketRequest(
        @NotNull(message = "Status is required")
        TicketStatus status,

        @Size(max = 500, message = "Admin note must be 500 characters or fewer")
        String adminNote,

        Long assignedToId
) {}
