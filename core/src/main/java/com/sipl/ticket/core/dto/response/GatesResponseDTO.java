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
public class GatesResponseDTO extends AuditDto{
    private Long id;
    private String gateNumber;
    private String gateDescription;
    private Boolean isExternal;
    private Long plantId;
    private String plantCode;
    private Boolean isActive;
}
