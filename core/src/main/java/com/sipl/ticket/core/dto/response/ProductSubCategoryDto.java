package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSubCategoryDto extends AuditDto {

    private Long productSubCategoryId;

    @NotNull(message = "productCategories information is required")
    private ProductCategoryDto productCategories;

    private String productSubCategoryName;

    private Boolean isActive;
}