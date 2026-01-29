package com.sipl.ticket.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sipl.ticket.core.dao.entity.Users;
import com.sipl.ticket.core.dao.entity.WorkFlowDefinition;
import com.sipl.ticket.core.dao.entity.WorkflowSteps;
import com.sipl.ticket.core.dto.request.UsersRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    private Users assignedUser;
}
