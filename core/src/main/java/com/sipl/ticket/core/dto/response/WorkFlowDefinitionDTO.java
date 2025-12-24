package com.sipl.ticket.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkFlowDefinitionDTO extends AuditDto{
    private Integer workFlowDefinitionId;
    private String name;
    private String entityType;
    private Boolean isActive;
}
