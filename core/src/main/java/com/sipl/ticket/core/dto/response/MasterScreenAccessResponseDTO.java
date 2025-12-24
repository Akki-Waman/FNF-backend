package com.sipl.ticket.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MasterScreenAccessResponseDTO {

    private Long masterId;
    private String masterName;
    private Boolean hasFullAccess;
    private List<MasterScreenCustomResponseDTO> values;
}
