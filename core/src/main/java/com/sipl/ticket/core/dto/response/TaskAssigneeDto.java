package com.sipl.ticket.core.dto.response;

import com.sipl.ticket.core.dao.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskAssigneeDto extends AuditDto{

    private Long taskAssigneeId;
    private TaskDto task;
    private UsersResponseDTO user;
}
