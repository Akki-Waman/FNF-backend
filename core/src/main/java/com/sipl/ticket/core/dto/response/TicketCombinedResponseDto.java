package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketCombinedResponseDto extends AuditDto{

    private Long ticketId;
    private String subject;
    private String description;
    private ContactResponseDto contact;
    private String complaintName;
    private String complaintMobileNo;
    private String emailAddress;
    private LocationResponseDTO location;
    private DepartmentResponseDTO department;
    private Long departmentId;
    private String departmentName;
    private BranchDto branch;
    private Long branchId;
    private String branchName;
    private ClientProductsResponseDTO clientProducts;
    private Long productId;
    private String productName;
    private ServiceResponseDTO service;
    private Long serviceId;
    private String serviceName;
    private UsersResponseDTO assignedTo;
    private Long assignedToId;
    private String assignedToName;
    private Integer status;
    private Integer priority;
    private List<Long> tagIds;
    private List<String> tagName;
    private List<String> ccEmails;

}
