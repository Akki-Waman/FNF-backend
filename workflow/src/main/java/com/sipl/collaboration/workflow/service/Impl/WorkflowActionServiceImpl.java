package com.sipl.collaboration.workflow.service.Impl;

import com.sipl.collaboration.core.dao.entity.*;
import com.sipl.collaboration.core.dao.repository.*;
import com.sipl.collaboration.core.dto.response.*;
import com.sipl.collaboration.core.enums.WorkFlowStatusEnum;
import com.sipl.collaboration.core.mapper.WorkflowActionMapper;
import com.sipl.collaboration.core.mapper.WorkflowInstanceMapper;
import com.sipl.collaboration.workflow.service.WorkFlowInstanceService;
import com.sipl.collaboration.workflow.service.WorkflowActionService;
import com.sipl.collaboration.workflow.service.EmailWorkflowService;
import com.sipl.collaboration.workflow.service.WorkflowNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkflowActionServiceImpl implements WorkflowActionService {
    private final WorkflowActionMapper workflowActionMapper;
    private final WorkflowActionRepository workflowActionRepository;
    private final WorkflowInstanceRepository workflowInstanceRepository;
    private final WorkflowStepsRepository workflowStepsRepository;
    private final WorkFlowInstanceService workflowInstanceService;
    private final WorkflowInstanceMapper workflowInstanceMapper;
    private final WorkflowNotificationService workflowNotificationService;
    private final EmailWorkflowService workflowEmailNotificationService;


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ApiResponseDTO<WorkflowActionDTO> processWorkflowAction(WorkflowActionDTO actionDto) {
        try {
            ApiResponseDTO<WorkflowActionDTO> actionResponse = addWorkflowAction(actionDto);
            WorkflowInstanceDTO instanceDto = actionDto.getWorkflowInstance();
            WorkflowStepsDTO currentStepDto = actionDto.getWorkflowStep();
            WorkflowSteps currentStep = null;
            WorkflowInstance workflowInstance =
                    workflowInstanceRepository
                            .findById(instanceDto.getWorkflowInstanceId())
                            .orElseThrow(
                                    () ->
                                            new RuntimeException(
                                                    "WorkflowInstance not found with ID: "
                                                            + instanceDto.getWorkflowInstanceId()));
            String stepStatus = null;
            String message;
            if (WorkFlowStatusEnum.APPROVED.getCode() == workflowInstance.getWorkFlowStatus() ||
                    WorkFlowStatusEnum.REJECTED.getCode() == (workflowInstance.getWorkFlowStatus())) {
                String statusName = WorkFlowStatusEnum.fromCode(workflowInstance.getWorkFlowStatus()).name();
                return new ApiResponseDTO<>(
                        null,
                        "Action cannot be performed. Workflow is already " + statusName + ".",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }
            if (WorkFlowStatusEnum.REJECTED.getCode() == actionDto.getAction()) {
                stepStatus = "REJECTED";
                workflowInstance.setWorkFlowStatus(WorkFlowStatusEnum.REJECTED.getCode());
                workflowInstance.setCompletedAt(LocalDateTime.now());
                currentStep = workflowInstance.getCurrentStep();
                message = String.format(
                        "Workflow has been Rejected."
                );
            } else if (WorkFlowStatusEnum.APPROVED.getCode() == actionDto.getAction()) {
                stepStatus = "IN_PROGRESS";
                currentStep =
                        workflowStepsRepository
                                .findById(currentStepDto.getWorkFlowStepsId())
                                .orElseThrow(
                                        () ->
                                                new RuntimeException(
                                                        "Current step not found: " + currentStepDto.getWorkFlowStepsId()));
                int currentOrder = currentStep.getStepOrder();
                int definitionId = currentStep.getWorkflowDefinition().getWorkFlowDefinitionId();
                workflowInstance.setWorkFlowStatus(WorkFlowStatusEnum.IN_PROGRESS.getCode());
                Optional<WorkflowSteps> nextStepOpt =
                        workflowStepsRepository.findNextStep(definitionId, currentOrder + 1);
                if (nextStepOpt.isPresent()) {
                    workflowInstance.setCurrentStep(nextStepOpt.get());
                    message = String.format(
                            "Workflow action Approved. Next step '%s' is ready for action.",
                            workflowInstance.getCurrentStep().getStepName()
                    );
                } else {
                    stepStatus = "APPROVED";
                    workflowInstance.setWorkFlowStatus(WorkFlowStatusEnum.APPROVED.getCode());
                    workflowInstance.setCompletedAt(LocalDateTime.now());
                    message = "Workflow has been approved and completed successfully.";
                }
            } else {
                return new ApiResponseDTO<>(null, "Please provide correct action 'Approved' or 'Rejected'.", HttpStatus.BAD_REQUEST, true);
            }
            WorkflowInstanceDTO updatedDto = workflowInstanceMapper.toDto(workflowInstance);
            ApiResponseDTO<WorkflowInstanceDTO> updateResponse =
                    workflowInstanceService.updateWorkflowInstance(updatedDto);
            WorkflowInstanceDTO savedInstanceDto = updateResponse.getData();
            log.info("workflowInstance {}", savedInstanceDto);
            Users user = workflowInstance.getCreatedBy();
            if (user == null || user.getId() == null) {
                throw new RuntimeException("CreatedBy user not set in WorkflowInstance.");
            }
            String mailStatus = null;
            if (stepStatus != null) {
                mailStatus = workflowEmailNotificationService.sendWorkflowEmail(workflowInstance, user, currentStep, stepStatus);
                if (mailStatus.equals("Failed")) {
                    log.error("Failed to send mail");
                    mailStatus = "Failed";
                }
            }
            createAndSaveWorkflowNotificationForActionProcess(
                    workflowInstance, currentStepDto, actionDto, mailStatus, stepStatus);
            return new ApiResponseDTO<>(actionResponse.getData(), message, HttpStatus.OK, false);
        } catch (Exception e) {
            log.error("Exception occurred at processWorkflowAction ", e);
            return new ApiResponseDTO<>(null, "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, true);
        }
    }

    public ApiResponseDTO<WorkflowActionDTO> addWorkflowAction(WorkflowActionDTO dto) {
        WorkflowAction entity = workflowActionMapper.toEntity(dto);
        WorkflowAction saved = workflowActionRepository.save(entity);
        WorkflowActionDTO savedDto = workflowActionMapper.toDto(saved);
        return new ApiResponseDTO<>(savedDto,  "Workflow Action added successfully.", HttpStatus.CREATED, false);
    }

    private void createAndSaveWorkflowNotificationForActionProcess(
            WorkflowInstance workflowInstance,
            WorkflowStepsDTO stepDto,
            WorkflowActionDTO actionDto,
            String mailStatus,String notificationReason) {
        Users user = workflowInstance.getCreatedBy();
        if (user == null || user.getId() == null) {
            throw new RuntimeException("CreatedBy user not set in WorkflowInstance.");
        }
        WorkflowNotificationDTO notificationDto = new WorkflowNotificationDTO();
        notificationDto.setInstance(workflowInstanceMapper.toDto(workflowInstance));
        notificationDto.setStep(stepDto);
        notificationDto.setUser(user);
        notificationDto.setNotificationReason(notificationReason);
        notificationDto.setStatus(mailStatus);
        notificationDto.setNotificationType("Email");
        notificationDto.setMessage("Cancellation Request " + (notificationReason != null ? notificationReason : "Actioned"));
        notificationDto.setSentAt(LocalDateTime.now());

        ApiResponseDTO<WorkflowNotificationDTO> notificationResponse =
                workflowNotificationService.addWorkflowNotification(notificationDto);
        if (notificationResponse.isError()) {
            throw new RuntimeException(
                    "Workflow notification creation failed for instanceId="
                            + workflowInstance.getWorkflowInstanceId());
        } else {
            log.info(
                    "Workflow notification created successfully for instanceId={}",
                    workflowInstance.getWorkflowInstanceId());
        }
    }

    public ApiResponseDTO<WorkflowActionDTO> updateWorkflowAction(WorkflowActionDTO dto) {
        try {
            Optional<WorkflowAction> optional =
                    workflowActionRepository.findById(dto.getWorkflowActionId());
            if (!optional.isPresent()) {
                return new ApiResponseDTO<>(null,   "Workflow Action not found", HttpStatus.NOT_FOUND, true);
            }

            WorkflowAction entityToUpdate = workflowActionMapper.toEntity(dto);
            WorkflowAction updated = workflowActionRepository.save(entityToUpdate);
            WorkflowActionDTO updatedDto = workflowActionMapper.toDto(updated);
            return new ApiResponseDTO<>(updatedDto,   "Workflow Action updated successfully.", HttpStatus.OK, false);

        } catch (Exception e) {
            log.error("Exception occurred at updateWorkflowAction ", e);
            return new ApiResponseDTO<>(null, "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, true);
        }
    }
}