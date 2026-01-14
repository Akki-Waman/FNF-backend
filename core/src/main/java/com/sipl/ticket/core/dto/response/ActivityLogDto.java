package com.sipl.ticket.core.dto.response;

import com.sipl.ticket.core.dao.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityLogDto extends AuditDto{

    private Long activityLogId;

    private String description;

    private String staffName;

    private String performedBy;

    private String ipAddress;
}
