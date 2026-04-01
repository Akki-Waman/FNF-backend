package com.sipl.ticket.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sipl.ticket.core.dao.entity.RbacUserRoles;
import com.sipl.ticket.core.dao.entity.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkflowStepsDTO extends AuditDto{
    private Integer workFlowStepsId;
    private WorkFlowDefinitionDTO workflowDefinition;
    private Integer stepOrder;
    private Integer assignmentMode;
    private String stepName;
    private Integer roleId;
    private String roleName;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private Boolean isFinalApprover;
}
