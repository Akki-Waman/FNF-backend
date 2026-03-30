package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reminder_recipients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReminderRecipient extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "channel_type_id")
    private Masters channelType;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Masters status;

    private LocalDateTime sentAt;

    private Integer retryCount ;

    private String lastError;

    private String userRole;

    @ManyToOne
    @JoinColumn(name = "reminder_id")
    private TicketReminder reminder;
}