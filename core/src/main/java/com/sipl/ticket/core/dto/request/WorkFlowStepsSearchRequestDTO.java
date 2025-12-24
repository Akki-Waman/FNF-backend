package com.sipl.ticket.core.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WorkFlowStepsSearchRequestDTO {
    private Integer pageNum;
    private Integer pageSize;
    private Integer workFlowStepsId;
    private Integer workFlowDefinitionId;
    private Integer stepOrder;
    private String stepName;
    private Long roleId;
    private Boolean isFinalApprover;

}
