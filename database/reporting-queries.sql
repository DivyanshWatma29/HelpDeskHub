-- =========================================================================
-- SupportDesk: SQL Reporting & Analytical Queries
-- Useful for operational dashboards and Capgemini Analyst interview practice
-- =========================================================================

-- 1. Multi-Table INNER JOIN: Tickets with Requester and Department details
-- Demonstrates normalization lookup across tickets, users, and departments.
SELECT 
    t.ticket_number,
    t.title,
    u.name AS requester_name,
    u.email AS requester_email,
    d.name AS department_name,
    t.priority,
    t.status,
    t.created_at
FROM tickets t
INNER JOIN users u ON t.requester_id = u.id
INNER JOIN departments d ON t.department_id = d.id
WHERE t.status = 'OPEN'
ORDER BY t.created_at ASC;

-- 2. Audit History JOIN: Complete lifecycle timeline for a specific ticket
-- Joins audit_logs with tickets and the user who made the change.
SELECT 
    t.ticket_number,
    a.action,
    a.old_status,
    a.new_status,
    a.note,
    COALESCE(u.name, 'System / Requester') AS changed_by,
    a.created_at AS logged_at
FROM audit_logs a
INNER JOIN tickets t ON a.ticket_id = t.id
LEFT JOIN users u ON a.changed_by_id = u.id
WHERE t.ticket_number = 'SD-00001'
ORDER BY a.created_at ASC;

-- 3. Department Workload & Resolution Metrics (GROUP BY + Conditional Aggregation)
-- Measures total tickets, resolved volume, and pending tickets per department.
SELECT 
    d.name AS department,
    COUNT(t.id) AS total_tickets,
    SUM(CASE WHEN t.status = 'RESOLVED' THEN 1 ELSE 0 END) AS resolved_count,
    SUM(CASE WHEN t.status <> 'RESOLVED' THEN 1 ELSE 0 END) AS pending_count,
    ROUND(SUM(CASE WHEN t.status = 'RESOLVED' THEN 1 ELSE 0 END) * 100.0 / NULLIF(COUNT(t.id), 0), 1) AS resolution_percentage
FROM departments d
LEFT JOIN tickets t ON d.id = t.department_id
GROUP BY d.id, d.name
ORDER BY total_tickets DESC;

-- 4. High-Priority SLA Queue
-- Tickets marked HIGH that are still OPEN or IN_PROGRESS.
SELECT 
    t.ticket_number,
    t.title,
    d.name AS department,
    u.name AS requester,
    TIMESTAMPDIFF(HOUR, t.created_at, NOW()) AS hours_open
FROM tickets t
JOIN departments d ON t.department_id = d.id
JOIN users u ON t.requester_id = u.id
WHERE t.priority = 'HIGH'
  AND t.status <> 'RESOLVED'
ORDER BY t.created_at ASC;

-- 5. Mean Time to Resolution (MTTR) by Department in Hours
SELECT 
    d.name AS department,
    ROUND(AVG(TIMESTAMPDIFF(MINUTE, t.created_at, t.updated_at)) / 60.0, 2) AS avg_resolution_hours,
    COUNT(t.id) AS resolved_ticket_count
FROM tickets t
JOIN departments d ON t.department_id = d.id
WHERE t.status = 'RESOLVED'
GROUP BY d.id, d.name;
