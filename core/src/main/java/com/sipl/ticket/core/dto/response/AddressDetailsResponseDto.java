package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDetailsResponseDto {

    private Long addressId;
    private Long customerId;
    private String addressLine1;
    private String addressLine2;
    private String area;
    private String city;
    private String state;
    private String country;
    private String gstNo;
    private String fax;
    private String emailId;
    private String pincode;
    private Long mobileNumber;
}
