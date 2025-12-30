package com.sipl.ticket.core.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class LocationResponseDTO extends AuditDto {

    private Long locationId;
    private String locationName;
    private String  locationType;
    private BigDecimal locationCapacity;
    private Boolean isActive;
//    private Long plantId;
//    private String plantCode;
}
