package com.sipl.ticket.core.dto.response;

import com.sipl.ticket.core.dao.entity.City;
import com.sipl.ticket.core.dao.entity.Country;
import com.sipl.ticket.core.dao.entity.State;
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

    private CountryResponseDto country;

    private StateResponseDto state;

    private CityResponseDto city;

    private Boolean isClient;

    private Boolean isDeleted;
}
