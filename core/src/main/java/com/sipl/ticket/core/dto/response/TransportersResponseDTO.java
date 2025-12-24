package com.sipl.ticket.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransportersResponseDTO extends AuditDto{
    private Long id;
    private String companyName;
    private String gstNumber;
    private String address;
    private String emailId;
    private String contactNumber;
    private Long transporterTypeId;
    private String transporterTypeCode;
    private Boolean isActive;
}
