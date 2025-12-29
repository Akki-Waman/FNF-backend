package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BrandDto extends AuditDto {
    private Long brandId;

    private String brandName;

    private Boolean isActive;
}

