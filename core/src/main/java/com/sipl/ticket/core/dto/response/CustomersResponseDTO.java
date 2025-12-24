package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomersResponseDTO extends AuditDto{

    private Long customerId;
    private String customerName;
    private String contactPersonName;
    private String panNumber;
    private String gstRegNumber;
    private String remarks;
    private Boolean status;
    private Long contactNumber;
    private String customerCode;
    private LocalDate registrationValidityDate;
    private String country;
    private List<AddressDetailsResponseDto> addressDetails;
}
