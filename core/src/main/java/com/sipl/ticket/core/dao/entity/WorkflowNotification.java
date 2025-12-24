package com.sipl.ticket.core.dao.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wfap_workflow_notification")
public class WorkflowNotification extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workflow_notification_id")
    private Integer workflowNotificationId;

    @ManyToOne
    @JoinColumn(name = "instance_id")
    private WorkflowInstance instance;

    @ManyToOne
    @JoinColumn(name = "step_id")
    private WorkflowSteps step;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "notification_type")
    private String notificationType;

    @Column(name = "message")
    private String message;

    @Column(name = "status")
    private String status;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "notification_reason")
    private String notificationReason;
}
