package com.supportdesk.repository;

import com.supportdesk.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    @Query("SELECT a FROM AuditLog a LEFT JOIN FETCH a.changedBy WHERE a.ticket.id = :ticketId ORDER BY a.createdAt ASC")
    List<AuditLog> findByTicketIdWithUser(@Param("ticketId") Long ticketId);
}
