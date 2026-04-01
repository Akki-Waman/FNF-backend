package com.sipl.ticket.core.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sipl.ticket.core.dto.response.AuditDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityLogDashboardDto extends AuditDto {

    private String message;
    private String module;

    @Override
    @JsonProperty("createdOn")
    public LocalDateTime getCreatedTime() {
        return super.getCreatedTime();
    }

    @Override
    @JsonIgnore
    public String getModifiedBy() {
        return super.getModifiedBy();
    }

    @Override
    @JsonIgnore
    public LocalDateTime getModifiedTime() {
        return super.getModifiedTime();
    }
}
