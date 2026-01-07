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

    @NotBlank(message = "Contact name is required")
    @Size(max = 150)
    private String contactName;

    @Email(message = "Invalid email format")
    @Size(max = 150)
    private String emailAddress;

    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Mobile number must be 10 digits"
    )
    private String mobileNo;

    @NotNull(message = "Department ID is mandatory")
    private Long departmentId;

    @NotNull(message = "Active status is mandatory")
    private Boolean isActive;

    private String departmentName;
}
