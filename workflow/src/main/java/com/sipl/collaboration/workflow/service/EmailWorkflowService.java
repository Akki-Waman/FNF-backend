package com.sipl.collaboration.workflow.service;

import com.sipl.collaboration.core.dao.entity.Users;
import com.sipl.collaboration.core.dao.entity.WorkflowInstance;
import com.sipl.collaboration.core.dao.entity.WorkflowSteps;
import org.springframework.stereotype.Service;

@Service
public interface EmailWorkflowService {
    String sendWorkflowEmail(
            WorkflowInstance workflowInstance,
            Users user,
            WorkflowSteps workflowSteps,
            String stepStatus);
}
