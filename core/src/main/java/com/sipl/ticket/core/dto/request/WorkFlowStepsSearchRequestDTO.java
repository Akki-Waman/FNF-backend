package com.sipl.ticket.core.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WorkFlowStepsSearchRequestDTO extends SearchRequestDto{
    private Integer workFlowStepsId;
    private Integer workFlowDefinitionId;
    private Integer stepOrder;
    private String stepName;
    private Integer roleId;
    private Boolean isFinalApprover;

}
