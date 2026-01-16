package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePenaltyResponseDTO {
    private Long ticketId;
    private String unitName;
    private String deviceName;
    private String service;
    private String subject;
    private LocalDate issueLogged;
    private LocalDate issueResolved;
    private Double resolutionTime;
    private String status;
    private String taskStatus;
    private Boolean withInWeek;
    private String penaltyDays;
    private Double penaltyPercentage;
}
