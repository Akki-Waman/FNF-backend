package com.sipl.ticket.core.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequestDto {

    private Long contactId;
    private String contactName;
    private String emailAddress;
    private String mobileNo;
    // FK reference
    private Long departmentId;
    private Boolean isActive;
    private String name;

}
