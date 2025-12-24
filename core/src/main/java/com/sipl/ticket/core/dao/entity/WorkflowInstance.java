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
@Table(name = "wfap_workflow_instance")
public class WorkflowInstance extends AuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer workflowInstanceId;

    @ManyToOne
    @JoinColumn(name = "wfap_workflow_id")
    private WorkFlowDefinition workflow;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "entity_type")
    private String entityType;

    @ManyToOne
    @JoinColumn(name = "current_step_id")
    private WorkflowSteps currentStep;

    @Column(name = "workflow_status")
    private Integer workFlowStatus;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    private String reason;
}
