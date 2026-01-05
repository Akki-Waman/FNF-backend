package com.sipl.ticket.core.dto.request;

import com.sipl.ticket.core.dao.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskSearchRequestDto extends SearchRequestDto {
    private Long ticketId;
    private String query; //taskId or taskName subject search
}