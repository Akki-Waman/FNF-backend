package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditDto implements Serializable {
    private String createdBy;
    private String modifiedBy;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
}