package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ticket_reminders")
@Audited
public class TicketReminder extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticket_id", nullable = false)
    private Long ticketId;

    @Column(name = "reminder_time", nullable = false)
    private LocalDateTime reminderTime;

    @Column(name = "is_recurring")
    private Boolean isRecurring;

    @Column(name = "recurrence_interval")
    private Integer recurrenceInterval;

    @Column(name = "recurrence_end_time")
    private LocalDateTime recurrenceEndTime;

    @Column(name = "next_run_time", nullable = false)
    private LocalDateTime nextRunTime;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "max_retry")
    private Integer maxRetry;

    @Column(name = "last_error")
    private String lastError;

    @OneToMany(
            mappedBy = "reminder",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ReminderRecipient> recipients;
}