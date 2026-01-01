package com.sipl.ticket.core.dto.request;

import lombok.Data;

@Data
public class ProductSubCategorySearchRequestDto extends SearchRequestDto {

    private Long productSubCategoryId;
}