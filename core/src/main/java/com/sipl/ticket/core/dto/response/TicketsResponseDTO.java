package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketsResponseDTO extends AuditDto {

    private Long ticketId;
    private String subject;
    private String description;
    private ContactResponseDto contact;
    private String complaintName;
    private String complaintMobileNo;
    private String emailAddress;
    private LocationResponseDTO location;
    private DepartmentResponseDTO department;
    private ClientProductsResponseDTO clientProducts;
    private Integer priority;
    private ServiceResponseDTO service;
    private UsersResponseDTO assignedTo;
    private Integer status;
    private BranchDto branch;
    private List<Long> tagIds;
    private String tags;
    private List<String> ccEmails;
    private String statusLabel;
    private String priorityLabel;

}
