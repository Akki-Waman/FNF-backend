package com.sipl.ticket.core.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ProcessStepDto {
    private String step;
    private String status;
    private String timestamp;
    private String value;

    public ProcessStepDto(String step, String status, String timestamp, String value) {
        this.step = step;
        this.status = status;
        this.timestamp = timestamp;
        this.value = value;
    }
}
