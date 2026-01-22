package com.sipl.ticket.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequestDto {

    private Long contactId;

    @Size(max = 150)
    private String contactName;

    @Size(max = 150)
    private String emailAddress;

    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Mobile number must be 10 digits"
    )
    private String mobileNo;

    private Long departmentId;

    private Boolean isActive;

    private String departmentName;

    private Integer branchId;
    private String branchName;
}
