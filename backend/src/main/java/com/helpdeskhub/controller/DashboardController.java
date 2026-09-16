package com.helpdeskhub.controller;

import com.helpdeskhub.dto.DashboardSummary;
import com.helpdeskhub.service.TicketService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final TicketService ticketService;

    public DashboardController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/summary")
    public DashboardSummary getSummary() {
        return ticketService.getDashboardSummary();
    }
}
