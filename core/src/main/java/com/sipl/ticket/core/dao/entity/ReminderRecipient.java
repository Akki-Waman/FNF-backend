package com.sipl.ticket.core.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reminder_recipients")
@Audited
public class ReminderRecipient extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reminderRecipientId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "channel_type_id", nullable = false)
    private Integer channelType;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "last_error")
    private String lastError;

    @Column(name = "user_role")
    private String userRole;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reminder_id", nullable = false)
    private TicketReminder reminder;
}