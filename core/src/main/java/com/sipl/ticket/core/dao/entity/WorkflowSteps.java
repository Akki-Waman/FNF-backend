package com.sipl.ticket.core.dao.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wfap_workflow_steps")
public class WorkflowSteps extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_flow_steps_id")
    private Integer workFlowStepsId;

    @ManyToOne
    @JoinColumn(name = "work_flow_definition_id", referencedColumnName = "work_flow_definition_id")
    private WorkFlowDefinition workflowDefinition;

    private Integer stepOrder;

    private String stepName;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Roles role;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;

    private Boolean isFinalApprover;
}
