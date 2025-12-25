package com.sipl.ticket.core.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class ShiftRequestDto {
    private Long shiftId;
    private String shiftName;
    private String description;
    private Boolean isActive;
}
