package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ticket_reminders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketReminder extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ticketId;

    private LocalDateTime reminderTime;

    private Boolean isRecurring = false;

    private Integer recurrenceInterval ;

    private LocalDateTime recurrenceEndTime;

    private LocalDateTime nextRunTime;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Masters status;

    private Integer retryCount;

    private Integer maxRetry ;

    private String lastError;

    private Long createdBy;

    @OneToMany(mappedBy = "reminder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReminderRecipient> recipients;
}