package com.sipl.ticket.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSubCategoryRequestDto {

    private Long productSubCategoryId;

    @NotNull(message = "Product category id is required")
    private Long productCategoryId;

    @NotBlank(message = "Product sub-category name is required")
    @Size(min = 2, max = 100, message = "Product sub-category name must be between 2 and 100 characters")
    private String productSubCategoryName;

    private Boolean isActive;
}
