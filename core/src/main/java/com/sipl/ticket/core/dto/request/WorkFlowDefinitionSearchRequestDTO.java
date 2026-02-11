package com.sipl.ticket.core.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WorkFlowDefinitionSearchRequestDTO extends SearchRequestDto{
    private String name;
    private String entityType;
}