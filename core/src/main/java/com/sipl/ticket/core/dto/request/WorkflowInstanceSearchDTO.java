package com.sipl.ticket.core.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowInstanceSearchDTO {
    private Integer pageNum;
    private Integer pageSize;
    private String entityType;
    private Integer workFlowStatus;
}
