package com.sipl.ticket.core.dto.request;

import lombok.Data;

@Data
public class ProductCategorySearchRequestDto extends SearchRequestDto {

    private Long productCategoryId;
}