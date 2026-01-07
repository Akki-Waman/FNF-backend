package com.sipl.ticket.core.dto.request;

import lombok.Data;

@Data
public class CompanySearchRequestDto extends SearchRequestDto {
    private Long companyId;
}
