package com.sipl.ticket.core.dto.request;

import lombok.Data;

@Data
public class BranchRequestDto {
    private Integer branchId;
    private String branchName;
    private String email;
    private String address;

    private Long companyId;
    private Long countryId;
    private Long stateId;
    private Long cityId;

    private Boolean isClient;
    private Boolean isActive;
}
