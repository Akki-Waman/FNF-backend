package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaqCategoryDto extends AuditDto {
    private Integer faqCategoryId;
    private String categoryName;
    private String categoryDescription;
    private Boolean isActive;
}
