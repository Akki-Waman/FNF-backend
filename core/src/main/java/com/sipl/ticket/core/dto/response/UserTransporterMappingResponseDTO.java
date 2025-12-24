package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTransporterMappingResponseDTO extends AuditDto{
    private Long id;
    private UsersResponseDTO user;
    private TransportersResponseDTO transporter;
    private Boolean isActive;
}
