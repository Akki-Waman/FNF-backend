package com.sipl.ticket.core.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnitRequestDto {

    private Long unitId;

    @NotBlank(message = "Unit name is required")
    @Size(max = 150)
    private String unitName;

    @NotNull(message = "Active status is mandatory")
    private Boolean isActive;
}
