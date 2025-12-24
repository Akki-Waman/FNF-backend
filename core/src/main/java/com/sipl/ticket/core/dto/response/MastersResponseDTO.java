package com.sipl.ticket.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MastersResponseDTO {
    private Long id;

    private String tblName;

    private String columnName;

    private Integer columnCode;

    private Integer columnValue;

    private String valueDesc;

    private Integer sequence;

    private Boolean isActive;
}
