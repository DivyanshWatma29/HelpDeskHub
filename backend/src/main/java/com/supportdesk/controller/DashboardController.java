package com.supportdesk.controller;

import com.supportdesk.dto.DashboardSummary;
import com.supportdesk.service.TicketService;
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
