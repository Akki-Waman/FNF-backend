package com.sipl.ticket.core.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CountrySearchRequestDto extends SearchRequestDto {

    private Long countryId;
    private String countryName;
    private String taxType;
    private Boolean isForeign;


}
