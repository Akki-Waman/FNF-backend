package com.sipl.ticket.core.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowInstanceSearchDTO extends SearchRequestDto{
    private String entityType;
    private Integer workFlowStatus;
}
