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
@Table(name = "wfap_workflow_action")
public class WorkflowAction extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer workflowActionId;

    @ManyToOne
    @JoinColumn(name = "instance_id")
    private WorkflowInstance workflowInstance;

    @ManyToOne
    @JoinColumn(name = "step_id")
    private WorkflowSteps workflowStep;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "action")
    private String action;

    @Column(name = "comments")
    private String comments;

    @Column(name = "action_time")
    private LocalDateTime actionTime;
}
