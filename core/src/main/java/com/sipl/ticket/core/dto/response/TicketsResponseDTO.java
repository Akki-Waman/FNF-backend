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
    private String priority = "Medium";
    private ServiceResponseDTO service;
    private UsersResponseDTO assignedTo;
    private String status = "Open";
    private BranchDto branch;
    private List<Long> tagIds;
    private List<String> ccEmails;
}
