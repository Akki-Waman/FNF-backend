package com.sipl.ticket.core.dto.request;

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
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isActive;
}
