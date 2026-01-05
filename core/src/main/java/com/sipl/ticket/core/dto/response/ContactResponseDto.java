package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactResponseDto {

    private Long contactId;
    private String contactName;
    private String emailAddress;
    private String mobileNo;


    private Long departmentId;
    private String departmentName;

    private Boolean isActive;


    private AuditDto auditDto;
}
