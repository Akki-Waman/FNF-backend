package com.sipl.ticket.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaqSearchRequestDto extends SearchRequestDto {
    private Integer faqCategoryId;
    private String question;
}
