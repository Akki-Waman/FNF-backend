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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime endTime;
    private Boolean isActive;
    private Integer branchId;
}
