package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Access;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffTicketResponseDTO {
    private String userName;
    private Integer totalAssignedTickets;
    private Integer openTickets;
    private Integer closedTickets;
    private Integer repliesToTickets;
    private Double averageReplyTime;
}
