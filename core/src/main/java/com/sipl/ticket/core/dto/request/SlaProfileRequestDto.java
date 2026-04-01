package com.sipl.ticket.core.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SlaProfileRequestDto {

    private Integer slaProfileId;

    private String profileName;

    private Integer branchId;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    private Boolean isActive;
}
