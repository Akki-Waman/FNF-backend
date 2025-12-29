package com.sipl.ticket.core.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryRequestDto {

    private Long productCategoryId;

    @NotBlank(message = "Product category name is required")
    @Size(min = 2, max = 100, message = "Product category name must be between 2 and 100 characters")
    private String productCategoryName;

    private Boolean isActive;
}

