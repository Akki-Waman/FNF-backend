package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto extends AuditDto {
    private Long companyId;
    private String companyName;
    private Boolean isActive;
}
