package com.sipl.ticket.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovementsResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer movementId;
    private Integer source;
    private Integer destination;
    private String movementDescription;
    private Boolean isSourceNet;
    private Boolean isPrimary;
}
