package com.sipl.ticket.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PackageDto extends AuditDto {
    private Long packageId;
    private String packageName;
    private String packageCode;
    private String packageType;
    private Double packageWeighment;
    private Double packageCapacity;
    private Boolean isActive;
    private OperatioalUnitDto unit;
    private Long plantId;
    private String plantCode;
}

