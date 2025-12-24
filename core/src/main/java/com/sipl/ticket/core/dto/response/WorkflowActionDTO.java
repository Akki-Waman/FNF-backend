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
public class WorkflowActionDTO extends AuditDto{
    private Integer workflowActionId;
    private WorkflowInstanceDTO workflowInstance;
    private WorkflowStepsDTO workflowStep;
    private Users user;
    private Integer action;
    private String comments;
    private LocalDateTime actionTime;
}
