package com.sipl.ticket.core.dto.response;

import com.sipl.ticket.core.dao.entity.TicketResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.AccessType;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseCcDTO extends AuditDto{

    private Long ticketResponseCcId;
    private TicketResponseDTO ticketResponse;
    private String email;
}
