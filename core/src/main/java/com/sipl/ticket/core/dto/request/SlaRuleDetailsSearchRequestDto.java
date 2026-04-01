package com.sipl.ticket.core.dto.request;

import lombok.Data;

@Data
public class SlaRuleDetailsSearchRequestDto {

    private Integer slaProfileId;
    private Boolean isActive;

    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "slaRuleDetailId";
    private String sortDir = "desc";
}
