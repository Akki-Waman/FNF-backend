package com.sipl.collaboration.workflow.service.Impl;

import com.sipl.collaboration.core.dao.entity.WorkflowNotification;
import com.sipl.collaboration.core.dao.repository.WorkflowNotificationRepository;
import com.sipl.collaboration.core.dto.request.WorkflowNotificationRequestDTO;
import com.sipl.collaboration.core.dto.response.ApiResponseDTO;
import com.sipl.collaboration.core.dto.response.PagedResponse;
import com.sipl.collaboration.core.dto.response.WorkflowNotificationDTO;
import com.sipl.collaboration.core.enums.WorkFlowStatusEnum;
import com.sipl.collaboration.core.mapper.WorkflowNotificationMapper;
import com.sipl.collaboration.workflow.service.EmailWorkflowService;
import com.sipl.collaboration.workflow.service.WorkflowNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkflowNotificationServiceImpl implements WorkflowNotificationService {

    private final WorkflowNotificationRepository workflowNotificationRepository;
    private final WorkflowNotificationMapper workflowNotificationMapper;
    private final EmailWorkflowService emailWorkflowService;

    @Override
    public ApiResponseDTO<WorkflowNotificationDTO> addWorkflowNotification(WorkflowNotificationDTO workflowNotificationDto) {
        log.info("<<START>> addWorkflowNotification called");

        Integer instanceId = workflowNotificationDto.getInstance().getWorkflowInstanceId();
        //Integer stepId = workflowNotificationDto.getStep().getWorkFlowStepsId();
        Long userId = workflowNotificationDto.getUser().getId();

        WorkflowNotification entity = workflowNotificationMapper.toEntity(workflowNotificationDto);
        WorkflowNotification savedEntity = workflowNotificationRepository.save(entity);
        WorkflowNotificationDTO savedDto = workflowNotificationMapper.toDto(savedEntity);
        log.info("<<END>> addWorkflowNotification success");

        return new ApiResponseDTO<>(savedDto, "Workflow notification service added successfully", HttpStatus.OK, false);

    }

    @Override
    public ApiResponseDTO<PagedResponse<WorkflowNotificationDTO>> searchWorkFlowStepsByPagination(
            WorkflowNotificationRequestDTO request) {

        try {
            Integer pageNum = Optional.ofNullable(request.getPageNum()).orElse(0);
            Integer pageSize = Optional.ofNullable(request.getPageSize()).orElse(10);

            log.info(
                    "Searching WorkflowNotifications - status: {}, notificationType: {}, workflowNotificationId: {}, pageNum: {}, pageSize: {}",
                    request.getStatus(),
                    request.getNotificationType(),
                    request.getWorkflowNotificationId(),
                    pageNum,
                    pageSize
            );

            Pageable pageable = PageRequest.of(
                    pageNum,
                    pageSize,
                    Sort.by(Sort.Direction.DESC, "workflowNotificationId")
            );

            Page<WorkflowNotification> resultPage =
                    workflowNotificationRepository.findBySearchQuery(
                            request.getStatus(),
                            request.getNotificationType(),
                            request.getWorkflowNotificationId(),
                            pageable
                    );

            if (resultPage.isEmpty()) {
                PagedResponse<WorkflowNotificationDTO> emptyResponse = new PagedResponse<>(
                        Collections.emptyList(),
                        resultPage.getNumber(),
                        resultPage.getTotalElements(),
                        resultPage.getTotalPages(),
                        resultPage.getSize(),
                        resultPage.isLast()
                );

                return new ApiResponseDTO<>(
                        emptyResponse,
                        "No workflow notifications found for the given filters.",
                        HttpStatus.OK,
                        false
                );
            }

            List<WorkflowNotificationDTO> dtoList = resultPage.getContent()
                    .stream()
                    .map(workflowNotificationMapper::toDto)
                    .collect(Collectors.toList());

            PagedResponse<WorkflowNotificationDTO> response = new PagedResponse<>(
                    dtoList,
                    resultPage.getNumber(),
                    resultPage.getTotalElements(),
                    resultPage.getTotalPages(),
                    resultPage.getSize(),
                    resultPage.isLast()
            );

            return new ApiResponseDTO<>(
                    response,
                    "Workflow notifications found successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (IllegalArgumentException ex) {
            return new ApiResponseDTO<>(
                    null,
                    "Invalid request: " + ex.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    true
            );
        } catch (Exception ex) {
            log.error("Exception occurred while searching workflow notifications: ", ex);
            return new ApiResponseDTO<>(
                    null,
                    "An unexpected error occurred while searching workflow notifications: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<Void> resendMail(Integer workFlowNotificationId) {
        log.info("<<START>> resendMail called");
        try {
            log.info("Workflow notification ID: {}", workFlowNotificationId);
            Optional<WorkflowNotification> fetchedWorkflowNotification =
                    workflowNotificationRepository.findById(workFlowNotificationId);

            if (fetchedWorkflowNotification.isEmpty()) {
                log.warn("Workflow notification not found for ID: {}", workFlowNotificationId);
                return new ApiResponseDTO<>(
                        null,
                        "Workflow notification not found",  HttpStatus.BAD_REQUEST,
                        true);
            }
            WorkflowNotification workflowNotification = fetchedWorkflowNotification.get();

            if (workflowNotification.getUser() == null) {
                log.error("User is null for WorkflowNotification ID: {}", workFlowNotificationId);
                return new ApiResponseDTO<>(
                        null,
                        "User not found for this workflow notification",  HttpStatus.INTERNAL_SERVER_ERROR,
                        true);
            }
            Integer statusCode = workflowNotification.getInstance().getWorkFlowStatus();
            WorkFlowStatusEnum statusEnum = WorkFlowStatusEnum.fromCode(statusCode);
            String statusName = statusEnum.name();
            String mailStatus = emailWorkflowService.sendWorkflowEmail(workflowNotification.getInstance(), workflowNotification.getUser(), workflowNotification.getInstance().getCurrentStep(), statusName);
            if(mailStatus.equals("Failed")) {
                log.error("Failed to send mail: {}");
                return new ApiResponseDTO<>(
                        null,
                        "Failed to resend mail",  HttpStatus.BAD_REQUEST,
                        true);
            }
            workflowNotification.setStatus(mailStatus);
            workflowNotificationRepository.save(workflowNotification);
            log.info("Mail resent successfully for WorkflowNotification ID: {}", workFlowNotificationId);
            return new ApiResponseDTO<>(
                    null,
                    "Mail resent successfully",  HttpStatus.OK,
                    false);
        } catch (Exception e) {
            log.error("Exception occurred in resendMail: ", e);
            return new ApiResponseDTO<>(
                    null,
                    "Error occurred in resendMail",  HttpStatus.INTERNAL_SERVER_ERROR,
                    true);
        }
    }

}
