package com.sipl.ticket.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StateRequestDto {

    private Long stateId;

    private String stateName;

    private Long countryId;

    private Boolean isActive;
}
