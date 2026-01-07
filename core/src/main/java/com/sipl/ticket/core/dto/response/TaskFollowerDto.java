package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskFollowerDto extends AuditDto{

    private Long taskFollowerId;
    private TaskDto task;
    private UsersResponseDTO user;
}
