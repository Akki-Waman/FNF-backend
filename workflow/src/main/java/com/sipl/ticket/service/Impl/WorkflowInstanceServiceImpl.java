package com.sipl.ticket.service.Impl;


import com.sipl.ticket.core.dao.entity.*;
import com.sipl.ticket.core.dao.repository.TicketRepository;
import com.sipl.ticket.core.dao.repository.UserRolesRepository;
import com.sipl.ticket.core.dao.repository.WorkflowInstanceRepository;
import com.sipl.ticket.core.dto.request.WorkflowInstanceSearchDTO;
import com.sipl.ticket.core.dto.response.*;
import com.sipl.ticket.core.enums.WorkFlowStatusEnum;
import com.sipl.ticket.core.helper.WorkflowInstanceExcelGenerator;
import com.sipl.ticket.core.mapper.WorkflowInstanceMapper;
import com.sipl.ticket.core.util.UserManager;
import com.sipl.ticket.service.EmailWorkflowService;
import com.sipl.ticket.service.WorkFlowInstanceService;
import com.sipl.ticket.service.WorkflowNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private final HttpServletRequest request;
    private final EmailWorkflowService emailWorkflowService;

    @Override
    public ApiResponseDTO<WorkflowInstanceDTO> addWorkflowInstance(WorkflowInstanceDTO dto) {
        WorkflowInstance entity = workflowInstanceMapper.toEntity(dto);
        entity.setCreatedBy(userManager.getUser(request));
        if (dto.getAssignedUser() != null) {
            entity.setAssignedUser(dto.getAssignedUser());
        }
        WorkflowInstance saved = workflowInstanceRepository.save(entity);
        WorkflowInstanceDTO savedDto = workflowInstanceMapper.toDto(saved);
        String mailStatus = null;
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
        return new ApiResponseDTO<>(null, "Workflow Instance updated successfully.", HttpStatus.OK, false);
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

            UserRoles mapping =
                    userRolesRepository.findFirstByUser_IdAndIsActiveTrueAndIsDeletedFalse(userMaster.getId());

            RbacUserRoles role = mapping != null ? mapping.getUserRole() : null;

            Integer roleId = role != null ? role.getUserRoleId() : null;

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
        log.info("User id " + userMaster.getId());
        UserRoles userRoleMappingEntity =
                userRolesRepository.findSingleByUserId(userMaster.getId());

        if (userRoleMappingEntity == null) {
            log.warn("No role mapped for userId={}", userMaster.getId());
            return new ApiResponseDTO<>(
                    null,
                    "User role not found. Please contact administrator.",
                    HttpStatus.NOT_FOUND,
                    true
            );
        }


        return null;
    }


    @Override
    public ApiResponseDTO<WorkflowInstanceDTO> deleteWorkflowInstanceById(Integer id) {
        try {
            Optional<WorkflowInstance> optional = workflowInstanceRepository.findById(id);
            if (!optional.isPresent()) {
                return new ApiResponseDTO<>(null, "WorkflowInstance not found", HttpStatus.NOT_FOUND, true);
            }

            WorkflowInstance entity = optional.get();

            workflowInstanceRepository.delete(entity);
            return new ApiResponseDTO<>(null, "Workflow Instance deleted successfully.", HttpStatus.OK, false);
        } catch (Exception e) {
            log.error("Exception occurred at deleteWorkflowInstance ", e);
            return new ApiResponseDTO<>(null, "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, true);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void exportWorkflowInstanceCsv(
            WorkflowInstanceSearchDTO request,
            HttpServletRequest servletRequest,
            HttpServletResponse response) {

        log.info("Request | entityType={} | workFlowStatus={}",
                request.getEntityType(),
                request.getWorkFlowStatus());

        try {
            Users userMaster = userManager.getUser(servletRequest);
            log.info("Logged-in User | userId={}", userMaster.getId());

            UserRoles userRoleMapping =
                    userRolesRepository.findSingleByUserId(userMaster.getId());

            Integer roleId = userRoleMapping != null
                    ? userRoleMapping.getUserRole().getUserRoleId()
                    : null;

            log.info("User Role | roleId={}", roleId);

            Page<WorkflowInstance> page =
                    workflowInstanceRepository.findByFilters(
                            request.getEntityType(),
                            request.getWorkFlowStatus(),
                            userMaster.getId(),
                            roleId,
                            Pageable.unpaged()
                    );

            log.info("DB Fetch Completed | totalElements={}", page.getTotalElements());

            List<WorkflowInstance> instances = page.getContent();

            if (instances.isEmpty()) {
                log.warn("No workflow instances found for given filters");
            } else {
                log.info("Records fetched | count={}", instances.size());
            }

            List<WorkflowInstanceDTO> dtos = instances.stream()
                    .map(workflowInstanceMapper::toDto)
                    .collect(Collectors.toList());

            log.info("Mapping completed | DTO count={}", dtos.size());

            WorkflowInstanceExcelGenerator.generateExcel(dtos, response);

            log.info("Excel generated successfully | file=workflow_instance.xlsx");

        } catch (Exception e) {
            log.error("Error while exporting workflow instances", e);
            throw new RuntimeException("Failed to export workflow instances", e);
        }

    }


}
