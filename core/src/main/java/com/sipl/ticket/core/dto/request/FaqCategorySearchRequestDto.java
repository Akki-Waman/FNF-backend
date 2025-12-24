package com.sipl.ticket.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaqCategorySearchRequestDto extends SearchRequestDto {
    private String categoryName;
    private Integer categoryId;
}


