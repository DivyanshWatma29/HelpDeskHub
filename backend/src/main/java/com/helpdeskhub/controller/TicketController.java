package com.helpdeskhub.controller;

import com.helpdeskhub.dto.CreateTicketRequest;
import com.helpdeskhub.dto.TicketDetailResponse;
import com.helpdeskhub.dto.TicketResponse;
import com.helpdeskhub.dto.UpdateTicketRequest;
import com.helpdeskhub.model.Priority;
import com.helpdeskhub.model.TicketStatus;
import com.helpdeskhub.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse createTicket(@Valid @RequestBody CreateTicketRequest request) {
        return ticketService.createTicket(request);
    }

    @GetMapping
    public List<TicketResponse> getTickets(
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) Priority priority) {
        return ticketService.getTickets(departmentId, status, priority);
    }

    @GetMapping("/{id}")
    public TicketDetailResponse getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }

    @GetMapping("/track/{ticketNumber}")
    public TicketDetailResponse trackTicket(@PathVariable String ticketNumber) {
        return ticketService.getTicketByNumber(ticketNumber);
    }

    @PutMapping("/{id}")
    public TicketResponse updateTicket(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTicketRequest request,
            Authentication authentication) {
        String adminUsername = authentication != null ? authentication.getName() : null;
        return ticketService.updateTicket(id, request, adminUsername);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
    }
}
