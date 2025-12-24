package com.sipl.ticket.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class YardResponseDTO extends AuditDto {
    private Long yardId;
    private String yardName;
    private String yardDescription;
    private Long plantId;
    private String plantCode;
    private Boolean isActive;
}
