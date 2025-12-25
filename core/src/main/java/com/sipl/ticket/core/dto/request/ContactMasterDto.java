package com.sipl.ticket.core.dto.request;

import com.sipl.ticket.core.dao.entity.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactMasterDto {

    private Long contactId;
    private String contactName;
    private String emailAddress;
    private String mobileNo;
    private String department;
    private Boolean isActive;
    private AuditEntity auditEntity;
}
