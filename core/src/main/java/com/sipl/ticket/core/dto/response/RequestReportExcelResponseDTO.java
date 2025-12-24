package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestReportExcelResponseDTO extends AuditDto{
    private Long RequestReportExcelId;

    private String reportType;
    private String requestObject;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Integer reportID;
    private Integer status;

    private String fileName;
    private String filePath;
}
