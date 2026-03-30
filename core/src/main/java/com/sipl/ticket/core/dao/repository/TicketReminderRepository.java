package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.TicketReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketReminderRepository extends JpaRepository<TicketReminder, Long> {

    @Query("SELECT r FROM TicketReminder r " +
            "WHERE r.nextRunTime <= :time " +
            "AND r.status = :status")
    List<TicketReminder> findDueReminders(@Param("time") LocalDateTime time,
                                          @Param("status") Integer status);


    List<TicketReminder> findByTicketId(Long ticketId);


    List<TicketReminder> findByStatus(Integer status);


    @Query("SELECT r FROM TicketReminder r " +
            "WHERE r.isRecurring = true " +
            "AND r.status = :status")
    List<TicketReminder> findActiveRecurringReminders(@Param("status") Integer status);


    List<TicketReminder> findByCreatedBy(Long createdBy);
}