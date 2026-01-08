package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlaProfileResponseDto extends AuditDto {

    private Integer slaProfileId;

    private BranchDto branch;

    private String profileName;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    private Boolean isActive;
}
