package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.TicketReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketReminderRepository extends JpaRepository<TicketReminder, Long> {

    @Query("SELECT r FROM TicketReminder r " +
            "LEFT JOIN FETCH r.recipients rec " +
            "WHERE r.nextRunTime <= :now " +
            "AND r.status = 0 " +
            "AND r.isActive = true " +
            "AND r.isDeleted = false")
    List<TicketReminder> findDueReminders(@Param("now") LocalDateTime reminderTime);

    @Query("SELECT r FROM TicketReminder r " +
            "LEFT JOIN FETCH r.recipients rec " +
            "WHERE r.ticket.ticketId = :ticketId " +
            "AND r.status = 0 " +
            "AND r.isActive = true " +
            "AND r.isDeleted = false")
    List<TicketReminder> findActiveByTicketId(@Param("ticketId") Long ticketId);


    @Query("SELECT r FROM TicketReminder r " +
            "WHERE r.status = 0 " +
            "AND r.isActive = true " +
            "AND r.isDeleted = false")
    List<TicketReminder> findAllActive();


    @Query("SELECT r FROM TicketReminder r " +
            "WHERE r.retryCount < r.maxRetry " +
            "AND r.status = 0 " +
            "AND r.isActive = true " +
            "AND r.isDeleted = false")
    List<TicketReminder> findRetryableReminders();
}