package com.sipl.ticket.core.dto.response;

import com.sipl.ticket.core.dao.entity.Zone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DivisionResponseDTO extends AuditDto{

    private Long divisionId;

    private String divisionName;

    private ZoneResponseDTO zone;

    private Long companyId;

    private String companyName;

    private Boolean isActive = true;
}
