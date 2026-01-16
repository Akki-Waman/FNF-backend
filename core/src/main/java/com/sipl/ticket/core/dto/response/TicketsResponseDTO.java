package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    /* ===== RESPONSE (REPLY) SLA ===== */

    private Double responseSlaHours;
    private Double responseTimeHours;
    private Boolean responseWithinSla;
    private Integer responsePenaltyTime;
    private BigDecimal responsePenaltyPercentage;
    private LocalDateTime responseDateTime;

    /* ===== RESOLUTION (CLOSE) SLA ===== */

    private Double resolutionSlaHours;
    private Double resolutionTimeHours;
    private Boolean resolutionWithinSla;
    private Integer resolutionPenaltyTime;
    private BigDecimal resolutionPenaltyPercentage;
    private LocalDateTime resolutionDateTime;

    private Boolean penaltyAllowed;
}
