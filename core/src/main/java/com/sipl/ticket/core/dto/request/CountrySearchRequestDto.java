package com.sipl.ticket.core.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CountrySearchRequestDto extends SearchRequestDto {

    private String query;


}
