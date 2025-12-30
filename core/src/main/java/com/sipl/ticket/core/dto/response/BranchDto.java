package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchDto extends AuditDto{
    private Integer branchId;

    private String branchName;

    private Boolean isActive;

    private String email;

    private String address;

    private CompanyDto company;

    private Integer countryId;

    private Integer stateId;

    private Integer cityId;
}
