package com.sipl.ticket.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportSearchRequestDTO {

    private String format;
    private ExportFilterDTO filters;
    private String search;
    private Integer branchId;
    private Integer status;
    private List<Long> companyIds;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

}