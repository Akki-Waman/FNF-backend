package com.sipl.ticket.core.dto.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShiftResponseDTO extends AuditDto {

        private Long shiftId;
        private String shiftName;
        private LocalTime startTime;
        private LocalTime endTime;
        private Boolean isActive;
        private Boolean isDeleted;
}




