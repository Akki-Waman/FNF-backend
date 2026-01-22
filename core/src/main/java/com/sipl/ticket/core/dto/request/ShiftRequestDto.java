package com.sipl.ticket.core.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sipl.ticket.core.dao.entity.Branches;
import lombok.*;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class ShiftRequestDto {
    private Long shiftId;
    private String shiftName;
    private String description;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private Boolean isActive;
    private Integer branchId;
}
