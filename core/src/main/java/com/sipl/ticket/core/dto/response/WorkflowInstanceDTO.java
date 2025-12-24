package com.sipl.ticket.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sipl.ticket.core.dao.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkflowInstanceDTO extends AuditDto{
    private Integer workflowInstanceId;
    private WorkFlowDefinitionDTO workflow;
    private Long entityId;
    private String entityType;
    private WorkflowStepsDTO currentStep;
    private Integer workFlowStatus;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String reason;
    private Users actionBy;
    private String lepNumber;
}
