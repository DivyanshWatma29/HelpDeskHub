package com.supportdesk.dto;

import com.supportdesk.model.Priority;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTicketRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must be 100 characters or fewer")
        String requesterName,

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email address")
        @Size(max = 255, message = "Email must be 255 characters or fewer")
        String requesterEmail,

        @NotNull(message = "Department is required")
        Long departmentId,

        @NotBlank(message = "Title is required")
        @Size(min = 4, max = 150, message = "Title must be between 4 and 150 characters")
        String title,

        @NotBlank(message = "Description is required")
        @Size(min = 10, max = 2000, message = "Description must contain 10 to 2000 characters")
        String description,

        @NotNull(message = "Priority is required")
        Priority priority
) {}
