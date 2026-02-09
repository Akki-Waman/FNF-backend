package com.sipl.ticket.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogReportRequestDto extends SearchRequestDto{
    private LocalDate fromDate;
    private LocalDate toDate;
}
