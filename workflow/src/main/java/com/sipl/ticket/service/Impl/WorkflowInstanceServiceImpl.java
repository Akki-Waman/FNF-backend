package com.sipl.ticket.service.Impl;


import com.sipl.ticket.core.dao.entity.*;
import com.sipl.ticket.core.dao.repository.TicketRepository;
import com.sipl.ticket.core.dao.repository.UserRolesRepository;
import com.sipl.ticket.core.dao.repository.WorkflowInstanceRepository;
import com.sipl.ticket.core.dto.request.WorkflowInstanceSearchDTO;
import com.sipl.ticket.core.dto.response.*;
import com.sipl.ticket.core.enums.WorkFlowStatusEnum;
import com.sipl.ticket.core.mapper.WorkflowInstanceMapper;
import com.sipl.ticket.core.util.UserManager;
import com.sipl.ticket.service.WorkFlowInstanceService;
import com.sipl.ticket.service.WorkflowNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class WorkflowInstanceServiceImpl implements WorkFlowInstanceService {

    private final WorkflowInstanceMapper workflowInstanceMapper;
    private final WorkflowInstanceRepository workflowInstanceRepository;
    private final WorkflowNotificationService workflowNotificationService;
    private final TicketRepository ticketRepository;
    private final UserRolesRepository userRolesRepository;
    private final @Qualifier("helperUserManager") UserManager userManager;

    @Override
    public ApiResponseDTO<WorkflowInstanceDTO> addWorkflowInstance(WorkflowInstanceDTO dto) {
        WorkflowInstance entity = workflowInstanceMapper.toEntity(dto);
        //TODO need to remove this logic in future after auditAware properly working
        entity.setCreatedBy(dto.getActionBy());
        WorkflowInstance saved = workflowInstanceRepository.save(entity);
        WorkflowInstanceDTO savedDto = workflowInstanceMapper.toDto(saved);
        return new ApiResponseDTO<>(savedDto, "Workflow Instance added successfully.", HttpStatus.CREATED, false);
    }

    private void createAndSaveWorkflowNotification(
            WorkflowInstanceDTO savedInstanceDto,
            WorkflowStepsDTO stepDto,
            Users user,
            String mailStatus)
    // throws RuntimeException
    {
        WorkflowNotificationDTO notificationDto = new WorkflowNotificationDTO();
        notificationDto.setInstance(savedInstanceDto);
        notificationDto.setStep(stepDto);
        notificationDto.setUser(user);
        notificationDto.setStatus(mailStatus);
        notificationDto.setNotificationType("Email");
        notificationDto.setMessage("");
        notificationDto.setSentAt(LocalDateTime.now());

        ApiResponseDTO<WorkflowNotificationDTO> notificationResponse =
                workflowNotificationService.addWorkflowNotification(notificationDto);
    }

    public ApiResponseDTO<WorkflowInstanceDTO> updateWorkflowInstance(WorkflowInstanceDTO dto) {
        Optional<WorkflowInstance> optional =
                workflowInstanceRepository.findById(dto.getWorkflowInstanceId());
        if (!optional.isPresent()) {
            throw new RuntimeException(
                    "WorkflowInstance not found with ID: " + dto.getWorkflowInstanceId());
        }
        WorkflowInstance entityToUpdate = optional.get();
        updateWorkflowInstanceFields(dto, entityToUpdate);
        WorkflowInstance updated = workflowInstanceRepository.save(entityToUpdate);
        WorkflowInstanceDTO updatedDto = workflowInstanceMapper.toDto(updated);
        return new ApiResponseDTO<>(null,"Workflow Instance updated successfully.", HttpStatus.OK, false);
    }

    private void updateWorkflowInstanceFields(
            WorkflowInstanceDTO dto, WorkflowInstance entityToUpdate) {
        if (dto.getWorkflow() != null && dto.getWorkflow().getWorkFlowDefinitionId() != null) {
            WorkFlowDefinition wfDef = new WorkFlowDefinition();
            wfDef.setWorkFlowDefinitionId(dto.getWorkflow().getWorkFlowDefinitionId());
            entityToUpdate.setWorkflow(wfDef);
        }

        if (dto.getEntityId() != null) {
            entityToUpdate.setEntityId(dto.getEntityId());
        }

        if (dto.getEntityType() != null) {
            entityToUpdate.setEntityType(dto.getEntityType());
        }

        if (dto.getCurrentStep() != null && dto.getCurrentStep().getWorkFlowStepsId() != null) {
            WorkflowSteps step = new WorkflowSteps();
            step.setWorkFlowStepsId(dto.getCurrentStep().getWorkFlowStepsId());
            entityToUpdate.setCurrentStep(step);
        }

        if (dto.getWorkFlowStatus() != null) {
            entityToUpdate.setWorkFlowStatus(dto.getWorkFlowStatus());
        }

        if (dto.getStartedAt() != null) {
            entityToUpdate.setStartedAt(dto.getStartedAt());
        }

        if (dto.getCompletedAt() != null) {
            entityToUpdate.setCompletedAt(dto.getCompletedAt());
        }
    }

    @Override
    public ApiResponseDTO<PagedResponse<WorkflowInstanceDTO>> searchWorkflowInstancesByPagination(
            WorkflowInstanceSearchDTO searchDto,
            HttpServletRequest servletRequest) {
        try {
            int pageNum = searchDto.getPageNum() != null ? searchDto.getPageNum() : 0;
            int pageSize = searchDto.getPageSize() != null ? searchDto.getPageSize() : 10;
            Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "workflowInstanceId"));

            ApiResponseDTO<WorkflowInstanceDTO> validationResponse = validateUser(servletRequest);
            if (validationResponse != null) {
                return new ApiResponseDTO<>(
                        null,
                        validationResponse.getMessage(),
                        validationResponse.getStatus(),
                        validationResponse.isError()
                );
            }
            Users userMaster = userManager.getUser(servletRequest);

            UserRoles userRoleMapping = userRolesRepository.findSingleByUserId(userMaster.getId());
            Long roleId = userRoleMapping != null ? userRoleMapping.getRole().getId() : null;

            Page<WorkflowInstance> page = workflowInstanceRepository.findByFilters(
                    searchDto.getEntityType() != null ? searchDto.getEntityType() : null,
                    searchDto.getWorkFlowStatus(),
                    userMaster.getId(),
                    roleId,
                    pageable
            );

            if (page.isEmpty()) {
                PagedResponse<WorkflowInstanceDTO> emptyResponse = new PagedResponse<>(
                        Collections.emptyList(),
                        page.getNumber(),
                        page.getTotalElements(),
                        page.getTotalPages(),
                        page.getSize(),
                        page.isLast()
                );

                return new ApiResponseDTO<>(
                        emptyResponse,
                        "Workflow instances not found.",
                        HttpStatus.OK,
                        false
                );
            }

            List<WorkflowInstanceDTO> dtos = page.getContent().stream()
                    .map(workflowInstanceMapper::toDto)
                    .collect(Collectors.toList());

            PagedResponse<WorkflowInstanceDTO> responseData = new PagedResponse<>(
                    dtos,
                    page.getNumber(),
                    page.getTotalElements(),
                    page.getTotalPages(),
                    page.getSize(),
                    page.isLast()
            );

            return new ApiResponseDTO<>(
                    responseData,
                    "SUCCESS",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("Error occurred while filtering workflow instances", e);
            return new ApiResponseDTO<>(
                    null,
                    "INTERNAL_SERVER_ERROR",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    private ApiResponseDTO<WorkflowInstanceDTO> validateUser(HttpServletRequest servletRequest) {
        Users userMaster = userManager.getUser(servletRequest);

        if (userMaster == null) {
            return new ApiResponseDTO<>(null, "User not found.", HttpStatus.NOT_FOUND, true);
        }

        UserRoles userRoleMappingEntity = userRolesRepository.findSingleByUserId(userMaster.getId());
        if (userRoleMappingEntity == null) {
            return new ApiResponseDTO<>(null, "User role not found. Please contact administrator.", HttpStatus.NOT_FOUND, true);
        }

        return null;
    }


    @Override
    public ApiResponseDTO<WorkflowInstanceDTO> deleteWorkflowInstanceById(Integer id) {
        try {
            Optional<WorkflowInstance> optional = workflowInstanceRepository.findById(id);
            if (!optional.isPresent()) {
                return new ApiResponseDTO<>(null,"WorkflowInstance not found", HttpStatus.NOT_FOUND, true);
            }

            WorkflowInstance entity = optional.get();

            workflowInstanceRepository.delete(entity);
            return new ApiResponseDTO<>(null,"Workflow Instance deleted successfully.", HttpStatus.OK, false);
        } catch (Exception e) {
            log.error("Exception occurred at deleteWorkflowInstance ", e);
            return new ApiResponseDTO<>(null,"INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, true);
        }
    }
}
