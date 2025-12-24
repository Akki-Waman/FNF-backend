package com.sipl.ticket.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlantMasterRequestDTO {
    private Long plantId;

    private String plantCode;

    private String plantDescription;

    private Boolean active;
}
