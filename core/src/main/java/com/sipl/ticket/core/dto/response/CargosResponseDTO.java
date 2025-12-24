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
public class CargosResponseDTO extends AuditDto{
    private Long id;
    private String materialCode;
    private String materialDesc;
    private String materialShortDesc;
    private String matType;
    private String divisionCode;
    private String hsnCode;
    private String hsnDescription;
    private Long unitId;
    private String unitName;
    private String batchFlag;
    private String intFlag;
    private Long cargoTypeId;
    private String cargoTypeName;
    private Boolean isActive;
}
