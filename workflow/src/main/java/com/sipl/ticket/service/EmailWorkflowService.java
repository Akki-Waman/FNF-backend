package com.sipl.ticket.service;

import com.sipl.ticket.core.dao.entity.Users;
import com.sipl.ticket.core.dao.entity.WorkflowInstance;
import com.sipl.ticket.core.dao.entity.WorkflowSteps;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.WorkflowInstanceDTO;
import org.springframework.stereotype.Service;

@Service
public interface EmailWorkflowService {
    String sendWorkflowEmail(
            WorkflowInstance workflowInstance,
            Users user,
            WorkflowSteps workflowSteps,
            String stepStatus);

}

