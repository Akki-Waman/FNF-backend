package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.TicketReminder;
import com.sipl.ticket.core.dao.entity.Masters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketReminderRepository extends JpaRepository<TicketReminder, Long> {

    // 🔥 Fetch due reminders (scheduler use)
    @Query("SELECT r FROM TicketReminder r " +
            "WHERE r.nextRunTime <= :time " +
            "AND r.status.columnName = 'PENDING'")
    List<TicketReminder> findDueReminders(@Param("time") LocalDateTime time);


    // 🔥 Fetch reminders by ticket
    List<TicketReminder> findByTicketId(Long ticketId);


    // 🔥 Fetch by status (dynamic master)
    List<TicketReminder> findByStatus(Masters status);


    // 🔥 Fetch active recurring reminders
    @Query("SELECT r FROM TicketReminder r " +
            "WHERE r.isRecurring = true " +
            "AND r.status.columnName = 'PENDING'")
    List<TicketReminder> findActiveRecurringReminders();


    // 🔥 Fetch reminders created by user
    List<TicketReminder> findByCreatedBy(Long createdBy);
}