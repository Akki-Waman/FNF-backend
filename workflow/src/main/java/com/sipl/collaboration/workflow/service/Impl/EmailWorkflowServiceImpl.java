package com.sipl.collaboration.workflow.service.Impl;

import com.sipl.collaboration.core.dao.entity.Transactions;
import com.sipl.collaboration.core.dao.entity.Users;
import com.sipl.collaboration.core.dao.entity.WorkflowInstance;
import com.sipl.collaboration.core.dao.entity.WorkflowSteps;
import com.sipl.collaboration.core.dao.repository.TransactionRepository;
import com.sipl.collaboration.core.dao.repository.UserRolesRepository;
import com.sipl.collaboration.core.dao.repository.WorkflowStepsRepository;
import com.sipl.collaboration.core.dto.request.MailSendRequestDTO;
import com.sipl.collaboration.core.dto.response.ApiResponseDTO;
import com.sipl.collaboration.core.enums.WorkFlowStatusEnum;
import com.sipl.collaboration.core.mapper.WorkflowInstanceMapper;
import com.sipl.collaboration.core.mapper.WorkflowNotificationMapper;
import com.sipl.collaboration.notificationService.service.EmailService;
import com.sipl.collaboration.workflow.service.EmailWorkflowService;
import com.sipl.collaboration.workflow.service.WorkflowNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailWorkflowServiceImpl implements EmailWorkflowService {

    private final WorkflowStepsRepository workflowStepsRepository;
     private final UserRolesRepository userRolesRepository;
     private final TransactionRepository transactionRepository;
     private final EmailService emailService;
     private final WorkflowInstanceMapper workflowInstanceMapper;

    @Value("${transaction_cancellation_initiated}")
    private Long transactionCancellationInitiatedTemplate;

    @Value("${transaction_cancellation_req_rejected}")
    private Long transactionCancellationReqRejectedTemplate;

    @Value("${transaction_cancellation_req_approved}")
    private Long transactionCancellationReqApprovedTemplate;

    @Value("${transaction_cancellation_req_next_stage}")
    private Long transactionCancellationReqNextStageTemplate;


    public String sendWorkflowEmail(
            WorkflowInstance workflowInstance,
            Users user,
            WorkflowSteps workflowSteps,
            String stepStatus) {
        Set<String> toSet=null;
        if(workflowInstance.getWorkFlowStatus()== WorkFlowStatusEnum.IN_PROGRESS.getCode())
        {
            toSet = prepareToNextStageInform(workflowInstance,user);
        }
        else {
            toSet = prepareToList(workflowInstance, user);
        }
        List<String> ccList = prepareCcList(workflowInstance, user);

        MailSendRequestDTO mailDto = buildMailDTO(workflowInstance, workflowSteps, user, stepStatus,
                new ArrayList<>(toSet), ccList);

        ApiResponseDTO<Void> response = emailService.sendMail(mailDto);
        String mailStatus = response.isError() ? "Failed" : "Sent";
        return mailStatus;
    }

    private Set<String> prepareToNextStageInform(WorkflowInstance workflowInstance,Users user) {
        Set<String> toSet = new LinkedHashSet<>();
        if (user.getEmailId() != null) toSet.add(user.getEmailId());

        Optional<WorkflowSteps> fetchedWorkFlowStep = workflowStepsRepository.findById(
                workflowInstance.getCurrentStep().getWorkFlowStepsId());
        if(fetchedWorkFlowStep.isEmpty())
        {
            log.info("Workflow step data not found");
        }
        WorkflowSteps step=fetchedWorkFlowStep.get();
            if (step.getRole() != null) {
                List<String> roleUsers = userRolesRepository.findActiveUsersByRoleId(step.getRole().getId())
                        .stream().map(Users::getEmailId).filter(Objects::nonNull).collect(Collectors.toList());
                toSet.addAll(roleUsers);
        }

        String initiatorMail = workflowInstance.getCreatedBy() != null
                ? workflowInstance.getCreatedBy().getEmailId() : null;
        if (initiatorMail != null) toSet.remove(initiatorMail);
        toSet.remove(user.getEmailId());

        return toSet;
    }

    private Set<String> prepareToList(WorkflowInstance workflowInstance, Users user) {
        Set<String> toSet = new LinkedHashSet<>();
        if (user.getEmailId() != null) toSet.add(user.getEmailId());

        List<WorkflowSteps> steps = workflowStepsRepository.findStepsByDefinitionId(
                workflowInstance.getWorkflow().getWorkFlowDefinitionId());
        for (WorkflowSteps step : steps) {
            if (step.getRole() != null) {
                List<String> roleUsers = userRolesRepository.findActiveUsersByRoleId(step.getRole().getId())
                        .stream().map(Users::getEmailId).filter(Objects::nonNull).collect(Collectors.toList());
                toSet.addAll(roleUsers);
            }
        }

        String initiatorMail = workflowInstance.getCreatedBy() != null
                ? workflowInstance.getCreatedBy().getEmailId() : null;
        if (initiatorMail != null) toSet.remove(initiatorMail);
        toSet.remove(user.getEmailId());

        return toSet;
    }

    private List<String> prepareCcList(WorkflowInstance workflowInstance, Users user) {
        List<String> ccList = new ArrayList<>();
        String initiatorMail = workflowInstance.getCreatedBy() != null
                ? workflowInstance.getCreatedBy().getEmailId() : null;
        if (initiatorMail != null) ccList.add(initiatorMail);
        return ccList;
    }

    private MailSendRequestDTO buildMailDTO(
            WorkflowInstance workflowInstance,
            WorkflowSteps workflowSteps,
            Users user,
            String stepStatus,
            List<String> toList,
            List<String> ccList) {

        Transactions transaction = transactionRepository.findById(workflowInstance.getEntityId())
                .orElseThrow(() -> new RuntimeException("Transaction ID not found: " + workflowInstance.getEntityId()));

        String userName = Optional.ofNullable(user.getFirstName()).orElse("") + " " +
                Optional.ofNullable(user.getLastName()).orElse("");
        String roleName = workflowSteps.getRole() != null ? workflowSteps.getRole().getName() : "Unknown Role";

        MailSendRequestDTO dto = new MailSendRequestDTO();
        dto.setNotifiedEmailId(toList);
        dto.setEmailIds(ccList);
        dto.setStatus(stepStatus);
        dto.setCancellationReason(workflowInstance.getReason());
        dto.setTransactionId(String.valueOf(workflowInstance.getEntityId()));
        dto.setSubject(workflowInstance.getEntityId() + " " + stepStatus);
        dto.setDate(LocalDate.now());
        dto.setCancellationInititatedTime(workflowInstance.getCreatedTime());
        dto.setLepIssueDate(transaction.getVehicleAllocation().getAllocationTime());
        dto.setLepNumber(transaction.getVehicleAllocation().getLepNumber());
        dto.setCancellationRequestBy(userName);
        dto.setTruckNumber(transaction.getVehicleAllocation().getVehicle().getVehicleRegistrationNumber());
        dto.setDriverName(transaction.getVehicleAllocation().getDriver().getDriverName());
        dto.setRfidNumber(transaction.getRfid().getRfidNumber());
        dto.setPerformedBy(userName + " (" + roleName + ")");
        switch (stepStatus) {
            case "REJECTED": dto.setTemplateId(transactionCancellationReqRejectedTemplate); break;
            case "APPROVED":dto.setTemplateId(transactionCancellationReqApprovedTemplate);break;
            case "IN_PROGRESS": dto.setTemplateId(transactionCancellationReqNextStageTemplate); break;
            case "CANCELLATION_INITIATED": dto.setTemplateId(transactionCancellationInitiatedTemplate); break;
        }
        return dto;
    }

}
