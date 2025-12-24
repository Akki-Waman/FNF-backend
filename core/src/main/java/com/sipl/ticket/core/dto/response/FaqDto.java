package com.sipl.ticket.core.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaqDto extends AuditDto {
    private Integer faqId;
    private FaqCategoryDto faqCategory;
    private String question;
    private String answer;
    private Boolean isActive;
    private String attachmentPath;
    private String imagePath;
}
