package com.sipl.ticket.core.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WorkFlowDefinitionSearchRequestDTO {
    private Integer pageNum;
    private Integer pageSize;
    private String name;
    private String entityType;
}