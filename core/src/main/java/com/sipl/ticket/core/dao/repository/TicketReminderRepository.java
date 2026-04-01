package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.TicketReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketReminderRepository extends JpaRepository<TicketReminder, Long> {


    @Query("SELECT COUNT(r) FROM TicketReminder r " +
            "WHERE r.ticket.ticketId = :ticketId " +
            "AND r.reminderTime = :reminderTime " +
            "AND r.isDeleted = false")
    Long countReminder(@Param("ticketId") Long ticketId,
                       @Param("reminderTime") LocalDateTime reminderTime);

    List<TicketReminder> findByTicket_TicketIdAndIsDeletedFalse(@Param("ticketId") Long ticketId);

    Optional<TicketReminder> findByTicketReminderIdAndIsDeletedFalse(Long id);

    @Query("SELECT COUNT(r) FROM TicketReminder r " +
            "WHERE r.ticket.ticketId = :ticketId " +
            "AND r.reminderTime = :reminderTime " +
            "AND r.ticketReminderId <> :reminderId " +
            "AND r.isDeleted = false")
    Long countDuplicateForUpdate(
            @Param("ticketId") Long ticketId,
            @Param("reminderTime") LocalDateTime reminderTime,
            @Param("reminderId") Long reminderId
    );
}