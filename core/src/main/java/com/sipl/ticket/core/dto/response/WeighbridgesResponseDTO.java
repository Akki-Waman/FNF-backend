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
public class WeighbridgesResponseDTO extends AuditDto{
    private Long id;
    private String weighbridgeName;
    private String weighbridgeDescription;
    private Long weighbridgeTypeId;
    private String weighbridgeType;
    private Boolean isActive;
    private Long plantId;
    private String plantCode;
}

