package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.TicketReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketReminderRepository extends JpaRepository<TicketReminder, Long> {

    @Query("SELECT r FROM TicketReminder r " +
            "LEFT JOIN FETCH r.recipients rec " +
            "WHERE r.nextRunTime <= :now " +
            "AND r.status = 0")
    List<TicketReminder> findDueReminders(LocalDateTime now);


    @Query("SELECT r FROM TicketReminder r " +
            "LEFT JOIN FETCH r.recipients rec " +
            "WHERE r.ticket.id = :ticketId " +
            "AND r.status = 0")
    List<TicketReminder> findActiveByTicketId(Long ticketId);


    @Query("SELECT r FROM TicketReminder r " +
            "WHERE r.status = 0")
    List<TicketReminder> findAllActive();


    @Query("SELECT r FROM TicketReminder r " +
            "WHERE r.retryCount < r.maxRetry " +
            "AND r.status = 0")
    List<TicketReminder> findRetryableReminders();
}