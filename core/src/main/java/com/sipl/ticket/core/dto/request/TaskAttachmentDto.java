package com.sipl.ticket.core.dto.request;

import com.sipl.ticket.core.dao.entity.DmsDocument;
import com.sipl.ticket.core.dto.response.AuditDto;
import com.sipl.ticket.core.dto.response.DmsDocumentDto;
import com.sipl.ticket.core.dto.response.TaskDto;
import lombok.Data;

@Data
public class TaskAttachmentDto extends AuditDto {
    private Long attachmentId;

    private TaskDto task;

    private DmsDocumentDto dmsDocument;
}
