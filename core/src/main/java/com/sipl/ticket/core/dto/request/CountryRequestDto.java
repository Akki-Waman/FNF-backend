package com.sipl.ticket.core.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CountryRequestDto {

    private Long countryId;

    @NotBlank(message = "Country name is mandatory")
    @Size(min = 2, max = 100, message = "Country name must be between 2 and 100 characters")
    private String countryName;


    private String taxType;


    private Boolean isForeign;


    private Boolean isActive;
}
