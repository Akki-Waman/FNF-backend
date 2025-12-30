package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductCategoryDto extends AuditDto {
    private Long productCategoryId;

    private String productCategoryName;

    private Boolean isActive;
}
