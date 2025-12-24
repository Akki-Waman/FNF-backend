package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestReportApiResponseDTO <T>{
    private T request;
    private Long reportId;
    private HttpStatus status;
    private String message;
    private boolean error;
    private String fileName;
    private String filePath;
}
