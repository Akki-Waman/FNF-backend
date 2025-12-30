package com.sipl.ticket.core.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UnitRequestDto {

    @NotBlank(message = "Unit name is mandatory")
    private String unitName;

    @NotNull(message = "Active flag is mandatory")
    private Boolean isActive;
}
