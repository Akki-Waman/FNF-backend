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
    private String severity;
    private String slaHours;
    private LocalDate issueLogged;
    private LocalDate responseOn;
    private String responseTime;
    private String status;
    private Boolean responseWithinSla;
    private Boolean responseWithin72Hours;
    private String penaltyTime;
    private Double penaltyPercentage;
}
