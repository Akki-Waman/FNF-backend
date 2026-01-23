package com.sipl.ticket.service.Impl;


import com.sipl.ticket.core.dao.entity.WorkflowSteps;
import com.sipl.ticket.core.dao.repository.UserRolesRepository;
import com.sipl.ticket.core.dao.repository.WorkflowStepsRepository;
import com.sipl.ticket.core.dto.request.WorkFlowStepsSearchRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.WorkflowStepsDTO;
import com.sipl.ticket.core.mapper.WorkflowStepsMapper;
import com.sipl.ticket.service.WorkflowStepsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkflowStepsServiceImpl implements WorkflowStepsService {

    private final WorkflowStepsRepository workflowStepsRepository;
    private final WorkflowStepsMapper workflowStepsMapper;
    private final UserRolesRepository roleMasterRepository;

    public ApiResponseDTO<WorkflowStepsDTO> addWorkflowSteps(WorkflowStepsDTO workflowStepsDto) {
        log.info("<<START>> addWorkflowSteps called");
        try {
            log.info("Role ID: {}", workflowStepsDto.getRole().getId());

            Integer workFlowDefId = workflowStepsDto.getWorkflowDefinition().getWorkFlowDefinitionId();
            Long roleId = workflowStepsDto.getRole().getId();
            Integer stepOrder = workflowStepsDto.getStepOrder();
            if (workflowStepsRepository.existsByStepOrderAndWorkflowDefinitionIdAndRoleId(
                    stepOrder, workFlowDefId, roleId)) {
                return new ApiResponseDTO<>(null, "Workflow Step with the same Step Order, Workflow Definition, and Role already exists.", HttpStatus.BAD_REQUEST, true);
            }
                WorkflowSteps workflowStepsToSave = workflowStepsMapper.toEntity(workflowStepsDto);

                log.debug("Saving new WorkflowSteps entity");
                WorkflowSteps savedDefinition = workflowStepsRepository.save(workflowStepsToSave);

                WorkflowStepsDTO responseDto = workflowStepsMapper.toDto(savedDefinition);

                log.info(
                        "WorkflowSteps added successfully with ID: {}", savedDefinition.getWorkFlowStepsId());
                return new ApiResponseDTO<>(responseDto, "Workflow Steps added successfully.", HttpStatus.CREATED, false);

        } catch (IllegalArgumentException e) {
            log.warn("Validation failed in addWorkflowSteps: {}", e.getMessage());
            return new ApiResponseDTO<>(null, "Workflow Steps added successfully.", HttpStatus.BAD_REQUEST, true);
        } catch (Exception e) {
            log.error("Exception occurred in addWorkflowSteps: ", e);
            return new ApiResponseDTO<>(null, "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, true);
        }
    }

    public ApiResponseDTO<WorkflowStepsDTO> updateWorkflowSteps(WorkflowStepsDTO workflowStepsDto) {
        log.info("<<START>> updateWorkflowSteps called");
        try {
            log.info("WorkFlowSteps ID: {}", workflowStepsDto.getWorkFlowStepsId());

            WorkflowSteps workflowSteps =
                    workflowStepsRepository.findWorkflowStepsId(workflowStepsDto.getWorkFlowStepsId());
            if (workflowSteps == null) {
                return new ApiResponseDTO<>(null, "Workflow Step not found with ID: " + workflowStepsDto.getWorkFlowStepsId(), HttpStatus.BAD_REQUEST, true);
            }
            workflowSteps = workflowStepsMapper.toEntity(workflowStepsDto);
            WorkflowSteps savedDefinition = workflowStepsRepository.save(workflowSteps);

            WorkflowStepsDTO responseDto = workflowStepsMapper.toDto(savedDefinition);

            log.info(
                    "WorkflowSteps updated successfully with ID: {}", savedDefinition.getWorkFlowStepsId());
            return new ApiResponseDTO<>(null,"Workflow Steps updated successfully.", HttpStatus.CREATED, false);

        } catch (Exception e) {
            log.error("Exception occurred in updateWorkflowSteps: ", e);
            return new ApiResponseDTO<>(null, "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, true);

        }
    }

    public ApiResponseDTO<WorkflowStepsDTO> getWorkFlowStepsById(Integer id) {
        log.info("<<START>> getWorkFlowStepsById called");
        try {
            log.info("WorkFlowSteps ID: {}", id);

            WorkflowSteps workflowSteps = workflowStepsRepository.findWorkflowStepsId(id);
            if (workflowSteps == null) {
                return new ApiResponseDTO<>(null, "Workflow Step not found with ID: " + id, HttpStatus.BAD_REQUEST, true);
            }
            WorkflowStepsDTO responseDto = workflowStepsMapper.toDto(workflowSteps);
                return new ApiResponseDTO<>(responseDto, "Workflow Steps found successfully.", HttpStatus.FOUND, false);
        } catch (Exception e) {
            log.error("Exception occurred in getWorkFlowStepsById: ", e);
                return new ApiResponseDTO<>(null, "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, true);
            }
    }

    public ApiResponseDTO<WorkflowStepsDTO> getAllWorkFlowSteps() {
        log.info("<<START>> getAllWorkFlowSteps called");
        try {
            List<WorkflowSteps> workflowSteps = workflowStepsRepository.findAll();
            if (workflowSteps.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        HttpStatus.BAD_REQUEST,
                        "Workflow Steps not founds",
                        true,
                        LocalDateTime.now()
                );
            }
            List<WorkflowStepsDTO> responseDto = workflowStepsMapper.toDtoList(workflowSteps);
            return new ApiResponseDTO<>(
                    responseDto,
                    HttpStatus.FOUND,
                    "Workflow Steps found successfully.",
                    false,
                    LocalDateTime.now()
            );
        } catch (Exception e) {
            log.error("Exception occurred in getAllWorkFlowSteps: ", e);
            return new ApiResponseDTO<>(
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error occured while getAllWorkFlowSteps",
                    true,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponseDTO<Void> deleteWorkFlowStepsById(Integer id) {
        log.info("<<START>> deleteWorkFlowStepsById called");
        try {
            log.info("WorkFlowSteps ID: {}", id);

            WorkflowSteps workflowSteps = workflowStepsRepository.findWorkflowStepsId(id);
            if (workflowSteps == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Workflow Step not found with ID: " + id,  HttpStatus.BAD_REQUEST,
                        true);
            }
            workflowStepsRepository.delete(workflowSteps);
            return new ApiResponseDTO<>(
                    null,
                    "Workflow Step delete successfully.",  HttpStatus.OK,
                    false);
        } catch (DataIntegrityViolationException e) {
            return new ApiResponseDTO<>(
                    null,
                    "Cannot delete workflow step because it is used in one or more workflow instances.",  HttpStatus.CONFLICT,
                    true);
        } catch (Exception e) {
            log.error("Exception occurred in deleteWorkFlowStepsById: ", e);
            return new ApiResponseDTO<>(
                    null,
                    "Error occured while deleteWorkFlowStepsById",  HttpStatus.INTERNAL_SERVER_ERROR,
                    true);
        }
    }

    @Override
    public ApiResponseDTO<PagedResponse<WorkflowStepsDTO>> searchWorkFlowStepsByPagination(
            WorkFlowStepsSearchRequestDTO request) {
        try {
            int pageNum = Optional.ofNullable(request.getPageNum()).orElse(0);
            int pageSize = Optional.ofNullable(request.getPageSize()).orElse(10);
            Pageable pageable = PageRequest.of(pageNum, pageSize);
            Page<WorkflowSteps> workflowStepsPage =
                    workflowStepsRepository.findBySearchQuery(
                            request.getStepName(),
                            request.getRoleId(),
                            request.getIsFinalApprover(),
                            request.getStepOrder(),
                            request.getWorkFlowDefinitionId(),
                            request.getWorkFlowStepsId(),
                            pageable
                    );

            if (workflowStepsPage.isEmpty()) {
                PagedResponse<WorkflowStepsDTO> emptyResponse = new PagedResponse<>(
                        Collections.emptyList(),
                        workflowStepsPage.getNumber(),
                        workflowStepsPage.getTotalElements(),
                        workflowStepsPage.getTotalPages(),
                        workflowStepsPage.getSize(),
                        workflowStepsPage.isLast()
                );

                return new ApiResponseDTO<>(
                        emptyResponse,
                        "No workflow steps found for the given filters.",
                        HttpStatus.OK,
                        false
                );
            }

            List<WorkflowStepsDTO> dtoList = workflowStepsPage.getContent()
                    .stream()
                    .map(workflowStepsMapper::toDto)
                    .collect(Collectors.toList());

            PagedResponse<WorkflowStepsDTO> response = new PagedResponse<>(
                    dtoList,
                    workflowStepsPage.getNumber(),
                    workflowStepsPage.getTotalElements(),
                    workflowStepsPage.getTotalPages(),
                    workflowStepsPage.getSize(),
                    workflowStepsPage.isLast()
            );

            return new ApiResponseDTO<>(
                    response,
                    "Workflow steps retrieved successfully.",
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
            log.error("An error occurred while searching workflow steps", ex);
            return new ApiResponseDTO<>(
                    null,
                    "An unexpected error occurred while searching workflow steps: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

}
