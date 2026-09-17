package com.supportdesk.repository;

import com.supportdesk.model.Priority;
import com.supportdesk.model.Ticket;
import com.supportdesk.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("""
        SELECT t FROM Ticket t
        JOIN FETCH t.department d
        JOIN FETCH t.requester r
        LEFT JOIN FETCH t.assignedTo a
        WHERE (:departmentId IS NULL OR d.id = :departmentId)
          AND (:status IS NULL OR t.status = :status)
          AND (:priority IS NULL OR t.priority = :priority)
        ORDER BY t.createdAt DESC
    """)
    List<Ticket> findByFilters(
            @Param("departmentId") Long departmentId,
            @Param("status") TicketStatus status,
            @Param("priority") Priority priority
    );

    @Query("""
        SELECT t FROM Ticket t
        JOIN FETCH t.department d
        JOIN FETCH t.requester r
        LEFT JOIN FETCH t.assignedTo a
        WHERE t.ticketNumber = :ticketNumber
    """)
    Optional<Ticket> findByTicketNumberWithDetails(@Param("ticketNumber") String ticketNumber);

    long countByStatus(TicketStatus status);
    long countByPriority(Priority priority);
    long countByDepartmentId(Long departmentId);
}
