package com.sipl.ticket.core.dto.request;

import lombok.Data;

@Data
public class SlaProfileSearchRequestDto extends SearchRequestDto {

    private Integer slaProfileId;

    private String profileName;

    private Integer branchId;

}
