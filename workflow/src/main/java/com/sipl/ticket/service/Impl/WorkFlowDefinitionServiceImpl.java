package com.sipl.ticket.service.Impl;


import com.sipl.ticket.core.dao.entity.WorkFlowDefinition;
import com.sipl.ticket.core.dao.repository.WorkFlowDefinitionRepository;
import com.sipl.ticket.core.dto.request.WorkFlowDefinitionSearchRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.WorkFlowDefinitionDTO;
import com.sipl.ticket.core.helper.WorkflowExcelGenerator;
import com.sipl.ticket.core.mapper.WorkFlowDefinitionMapper;
import com.sipl.ticket.core.util.PaginationUtil;
import com.sipl.ticket.service.WorkFlowDefinitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorkFlowDefinitionServiceImpl implements WorkFlowDefinitionService {

    private final WorkFlowDefinitionRepository workFlowDefinitionRepository;
    private final WorkFlowDefinitionMapper workFlowDefinitionMapper;

    public ApiResponseDTO<WorkFlowDefinitionDTO> addWorkFlowDefinition(
            WorkFlowDefinitionDTO workFlowDefinitionDto) {
        log.info("<<START>> addWorkFlowDefinition called");
        try {
            log.debug("Validating input for addWorkFlowDefinition");
            validateWorkFlowDefinitionInput(workFlowDefinitionDto);

            WorkFlowDefinition workFlowDefinitionToSave =
                    workFlowDefinitionMapper.toEntity(workFlowDefinitionDto);

            log.debug("Saving new WorkFlowDefinition entity");
            WorkFlowDefinition savedDefinition =
                    workFlowDefinitionRepository.save(workFlowDefinitionToSave);

            WorkFlowDefinitionDTO responseDto = workFlowDefinitionMapper.toDto(savedDefinition);

            log.info(
                    "WorkFlowDefinition added successfully with ID: {}",
                    savedDefinition.getWorkFlowDefinitionId());
            return new ApiResponseDTO<>(
                    responseDto,
                    "Workflow definition added successfully.", HttpStatus.CREATED,
                    false);
        } catch (IllegalArgumentException e) {
            log.warn("Validation failed in addWorkFlowDefinition: {}", e.getMessage());
            return new ApiResponseDTO<>(
                    null,
                    e.getMessage(), HttpStatus.BAD_REQUEST,
                    true);
        } catch (Exception e) {
            log.error("Exception occurred in addWorkFlowDefinition: ", e);
            return new ApiResponseDTO<>(
                    null,
                    null, HttpStatus.INTERNAL_SERVER_ERROR,
                    true);
        }
    }

    private void validateWorkFlowDefinitionInput(WorkFlowDefinitionDTO dto) {
        log.debug("Validating name and entityType");

        if (dto.getName() == null || dto.getName().isBlank()) {
            log.warn("Workflow name is missing");
            throw new IllegalArgumentException("Workflow name is required.");
        }

        if (dto.getEntityType() == null || dto.getEntityType().isBlank()) {
            log.warn("Entity type is missing");
            throw new IllegalArgumentException("Entity type is required.");
        }

        if (dto.getIsActive() == null) {
            log.warn("isActive status is missing");
            throw new IllegalArgumentException("Status (isActive) must be provided.");
        }

        if (workFlowDefinitionRepository.existsByEntityType(dto.getEntityType())) {
            log.warn("Workflow with entityType '{}' already exists", dto.getEntityType());
            throw new IllegalArgumentException(
                    "Workflow definition for this entity type already exists.");
        }
        if (workFlowDefinitionRepository.existsByWorkFlowName(dto.getName())) {
            log.warn("Workflow with name '{}' already exists", dto.getName());
            throw new IllegalArgumentException("Workflow definition for this name already exists.");
        }
    }

    public ApiResponseDTO<WorkFlowDefinitionDTO> updateWorkFlowDefinition(WorkFlowDefinitionDTO dto) {
        log.info(
                "<<START>> updateWorkFlowDefinition called for ID: {} <<START>>",
                dto.getWorkFlowDefinitionId());

        try {
            if (dto.getWorkFlowDefinitionId() == null) {
                log.warn("WorkFlowDefinitionId is missing in update payload.");
                throw new IllegalArgumentException("WorkFlowDefinitionId is required for update.");
            }

            WorkFlowDefinition existing =
                    workFlowDefinitionRepository
                            .findById(dto.getWorkFlowDefinitionId())
                            .orElseThrow(
                                    () -> {
                                        log.warn(
                                                "No WorkFlowDefinition found with ID: {}", dto.getWorkFlowDefinitionId());
                                        return new IllegalArgumentException(
                                                "WorkFlowDefinition not found with ID: " + dto.getWorkFlowDefinitionId());
                                    });

            validateDuplicateWorkflowDefinitionFields(dto);
            log.debug("Updating WorkFlowDefinition fields for ID: {}", dto.getWorkFlowDefinitionId());
            updateWorkFlowDefinitionFields(existing, dto);

            WorkFlowDefinition updated = workFlowDefinitionRepository.save(existing);
            WorkFlowDefinitionDTO responseDto = workFlowDefinitionMapper.toDto(updated);

            log.info("WorkFlowDefinition updated successfully for ID: {}", dto.getWorkFlowDefinitionId());
            return new ApiResponseDTO<>(
                    responseDto,
                    "Workflow definition updated successfully.", HttpStatus.OK,
                    false);

        } catch (IllegalArgumentException e) {
            log.warn("Validation failed in updateWorkFlowDefinition: {}", e.getMessage());
            return new ApiResponseDTO<>(
                    null,
                    e.getMessage(), HttpStatus.BAD_REQUEST,
                    true);

        } catch (Exception e) {
            log.error("Exception occurred in updateWorkFlowDefinition: ", e);
            return new ApiResponseDTO<>(
                    null,
                    "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR,
                    true);
        }
    }

    private void validateDuplicateWorkflowDefinitionFields(WorkFlowDefinitionDTO dto) {
        Integer currentId = dto.getWorkFlowDefinitionId();

        if (dto.getEntityType() != null
                && workFlowDefinitionRepository.existsByEntityTypeAndNotId(
                dto.getEntityType(), currentId)) {
            log.warn("Workflow with entityType '{}' already exists", dto.getEntityType());
            throw new IllegalArgumentException(
                    "Workflow definition for this entity type already exists.");
        }

        if (dto.getName() != null
                && workFlowDefinitionRepository.existsByWorkFlowNameAndNotId(dto.getName(), currentId)) {
            log.warn("Workflow with name '{}' already exists", dto.getName());
            throw new IllegalArgumentException("Workflow definition for this name already exists.");
        }
    }

    private void updateWorkFlowDefinitionFields(
            WorkFlowDefinition existing, WorkFlowDefinitionDTO dto) {
        if (dto.getName() != null) {
            log.debug("Updating name: oldValue = {}, newValue = {}", existing.getName(), dto.getName());
            existing.setName(dto.getName());
        }

        if (dto.getEntityType() != null) {
            log.debug(
                    "Updating entityType: oldValue = {}, newValue = {}",
                    existing.getEntityType(),
                    dto.getEntityType());
            existing.setEntityType(dto.getEntityType());
        }

        if (dto.getIsActive() != null) {
            log.debug(
                    "Updating isActive: oldValue = {}, newValue = {}",
                    existing.getIsActive(),
                    dto.getIsActive());
            existing.setIsActive(dto.getIsActive());
        }
    }

    public ApiResponseDTO<WorkFlowDefinitionDTO> getWorkFlowDefinitionById(Integer id) {
        log.info("<<START>> getWorkFlowDefinitionById called for ID: {} <<START>>", id);
        try {
            WorkFlowDefinition entity =
                    workFlowDefinitionRepository
                            .findById(id)
                            .filter(WorkFlowDefinition::getIsActive)
                            .orElseThrow(
                                    () ->
                                            new IllegalArgumentException(
                                                    "Active workflow definition not found with ID: " + id));

            WorkFlowDefinitionDTO dto = workFlowDefinitionMapper.toDto(entity);

            log.info("WorkFlowDefinition retrieved successfully for ID: {}", id);
            return new ApiResponseDTO<>(
                    dto,
                    "Workflow definition retrieved successfully.", HttpStatus.OK,
                    false);
        } catch (IllegalArgumentException e) {
            log.warn("Not found in getWorkFlowDefinitionById: {}", e.getMessage());
            return new ApiResponseDTO<>(
                    null,
                    e.getMessage(), HttpStatus.NOT_FOUND,
                    true);
        } catch (Exception e) {
            log.error("Exception occurred in getWorkFlowDefinitionById: ", e);
            return new ApiResponseDTO<>(
                    null,
                    "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR,
                    true);
        }
    }

    public ApiResponseDTO<WorkFlowDefinitionDTO> getAllWorkFlowDefinitions() {
        log.info("<<START>> getAllWorkFlowDefinitions called <<START>>");
        try {
            List<WorkFlowDefinition> activeEntities = workFlowDefinitionRepository.findByIsActiveTrue();
            if (activeEntities.isEmpty()) {
                log.info("No active workflow definitions found");
                return new ApiResponseDTO<>(
                        null,
                        HttpStatus.NOT_FOUND,
                        "No active workflow definitions found.",
                        true,
                        LocalDateTime.now()
                );
            }
            List<WorkFlowDefinitionDTO> dtos =
                    activeEntities.stream().map(workFlowDefinitionMapper::toDto).collect(Collectors.toList());

            log.info("Retrieved {} active workflow definitions", dtos.size());
            return new ApiResponseDTO<>(
                    dtos,
                    HttpStatus.OK,
                    "Active workflow definitions retrieved successfully.",
                    false,
                    LocalDateTime.now()
            );
        } catch (Exception e) {
            log.error("Exception occurred in getAllWorkFlowDefinitions: ", e);
            return new ApiResponseDTO<>(
                    null,
                    HttpStatus.NOT_FOUND,
                    "No active workflow definitions found.",
                    true,
                    LocalDateTime.now()
            );
        }
    }


    public ApiResponseDTO<Void> deleteWorkFlowDefinitionById(Integer id) {
        log.info("<<START>> deleteWorkFlowDefinitionById called for ID: {} <<START>>", id);
        try {
            WorkFlowDefinition existing =
                    workFlowDefinitionRepository
                            .findById(id)
                            .orElseThrow(
                                    () ->
                                            new IllegalArgumentException("Workflow definition not found with ID: " + id));

            workFlowDefinitionRepository.delete(existing);

            log.info("WorkFlowDefinition-deleted successfully for ID: {}", id);
            return new ApiResponseDTO<>(
                    null,
                    "Workflow definition deleted successfully.", HttpStatus.OK,
                    false);
        } catch (DataIntegrityViolationException e) {
            return new ApiResponseDTO<>(
                    null,
                    "Cannot delete workflow definition because it is being used in one or more workflow instances.", HttpStatus.CONFLICT,
                    false);
        } catch (IllegalArgumentException e) {
            log.warn("Not found in deleteWorkFlowDefinitionById: {}", e.getMessage());
            return new ApiResponseDTO<>(
                    null,
                    e.getMessage(), HttpStatus.NOT_FOUND,
                    true);
        } catch (Exception e) {
            log.error("Exception occurred in deleteWorkFlowDefinitionById: ", e);
            return new ApiResponseDTO<>(
                    null,
                    "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR,
                    true);
        }
    }

    @Override
    public ApiResponseDTO<PagedResponse<WorkFlowDefinitionDTO>> searchWorkFlowDefinitionsByPagination(
            WorkFlowDefinitionSearchRequestDTO request) {

        try {
            Pageable pageable = PaginationUtil.pageable(
                    request.getPage(),
                    request.getSize(),
                    request.getSortBy(),
                    request.getSortDir()
            );

            log.info(
                    "Searching WorkFlowDefinitions - name: {}, entityType: {}, pageNum: {}, pageSize: {}",
                    request.getName(),
                    request.getEntityType(),
                    request.getPage(),
                    request.getSize()
            );

            Page<WorkFlowDefinition> resultPage =
                    workFlowDefinitionRepository.searchWorkFlowDefinitions(
                            request.getName(),
                            request.getEntityType(),
                            pageable
                    );

            if (resultPage.isEmpty()) {
                PagedResponse<WorkFlowDefinitionDTO> emptyResponse = new PagedResponse<>(
                        Collections.emptyList(),
                        resultPage.getNumber(),
                        resultPage.getTotalElements(),
                        resultPage.getTotalPages(),
                        resultPage.getSize(),
                        resultPage.isLast()
                );

                return new ApiResponseDTO<>(
                        emptyResponse,
                        "No workflow definitions found for the given filters.",
                        HttpStatus.OK,
                        false
                );
            }

            List<WorkFlowDefinitionDTO> dtoList = resultPage.getContent()
                    .stream()
                    .map(workFlowDefinitionMapper::toDto)
                    .collect(Collectors.toList());

            PagedResponse<WorkFlowDefinitionDTO> response = new PagedResponse<>(
                    dtoList,
                    resultPage.getNumber(),
                    resultPage.getTotalElements(),
                    resultPage.getTotalPages(),
                    resultPage.getSize(),
                    resultPage.isLast()
            );

            return new ApiResponseDTO<>(
                    response,
                    "Workflow definitions found successfully",
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
            log.error("Exception occurred while searching workflow definitions: ", ex);
            return new ApiResponseDTO<>(
                    null,
                    "An unexpected error occurred while searching workflow definitions: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void exportWorkFlowDefinitionCsv(
            WorkFlowDefinitionSearchRequestDTO request,
            HttpServletResponse response) {

        try {
            log.info("Export request received | name={} | entityType={}",
                    request.getName(), request.getEntityType());

            Page<WorkFlowDefinition> page =
                    workFlowDefinitionRepository.searchWorkFlowDefinitions(
                            request.getName(),
                            request.getEntityType(),
                            Pageable.unpaged()
                    );

            log.info("Search executed successfully");
            log.info("Total elements fetched = {}", page.getTotalElements());
            log.info("Records in current export batch = {}", page.getContent().size());

            List<WorkFlowDefinition> workflows = page.getContent();

            if (workflows.isEmpty()) {
                log.warn("No workflow records found for given search criteria");
            }

            List<WorkFlowDefinitionDTO> dtos = new ArrayList<>();
            for (WorkFlowDefinition wf : workflows) {
                dtos.add(workFlowDefinitionMapper.toDto(wf));
            }

            log.info("Mapping completed | DTO count = {}", dtos.size());

            WorkflowExcelGenerator.generateExcel(dtos, response);

            log.info("Excel generated successfully | file=workflows.xlsx");

        } catch (Exception e) {
            log.error("Error while exporting workflows to Excel", e);
            throw new RuntimeException("Failed to export workflows CSV", e);
        }
    }



}
