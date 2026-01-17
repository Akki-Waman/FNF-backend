package com.sipl.ticket.service.impl;

import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.core.dao.entity.Department;
import com.sipl.ticket.core.dao.repository.DepartmentRepository;
import com.sipl.ticket.core.dto.request.DepartmentRequestDto;
import com.sipl.ticket.core.dto.request.DepartmentSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.DepartmentResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.helper.DepartmentExcelGenerator;
import com.sipl.ticket.core.mapper.DepartmentMapper;
import com.sipl.ticket.core.util.PaginationUtil;
import com.sipl.ticket.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repository;
    private final DepartmentMapper mapper;

    @Override
    @CacheEvict(value = "departments", allEntries = true)
    @ActivityLoggable(
            action = "CREATE",
            module = "DEPARTMENT",
            description = "Department {0} created successfully"
    )
    public ApiResponseDTO<DepartmentResponseDTO> saveDepartment(DepartmentRequestDto dto) {

        log.info("Saving department with name: {}", dto.getDepartmentName());

        try {
            String name = dto.getDepartmentName().trim();

            if (repository.existsByDepartmentNameIgnoreCase(name)) {
                return new ApiResponseDTO<>(
                        null,
                        "Department '" + name + "' already exists.",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            Department department = mapper.toEntity(dto);
            department.setIsActive(true);

            Department saved = repository.save(department);

            return new ApiResponseDTO<>(
                    mapper.toDto(saved),
                    "Department created successfully",
                    HttpStatus.CREATED,
                    false
            );

        } catch (Exception e) {
            log.error("Error occurred while saving department", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @CacheEvict(value = "departments", allEntries = true)
    @ActivityLoggable(
            action = "UPDATE",
            module = "DEPARTMENT",
            description = "Department {0} updated successfully"
    )
    public ApiResponseDTO<DepartmentResponseDTO> updateDepartment(DepartmentRequestDto dto) {

        log.info("Updating department, id={}, name={}, isActive={}",
                dto != null ? dto.getDepartmentId() : null,
                dto != null ? dto.getDepartmentName() : null,
                dto != null ? dto.getIsActive() : null);

        try {
            if (dto == null || dto.getDepartmentId() == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Department ID is required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            Department department = repository
                    .findById(dto.getDepartmentId())
                    .orElse(null);

            if (department == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Department not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (dto.getDepartmentName() != null &&
                    !dto.getDepartmentName().trim().isEmpty()) {

                String name = dto.getDepartmentName().trim();

                if (repository.existsByDepartmentNameIgnoreCaseAndDepartmentIdNot(
                        name, dto.getDepartmentId())) {

                    return new ApiResponseDTO<>(
                            null,
                            "Department with the name '" + name + "' already exists",
                            HttpStatus.CONFLICT,
                            true
                    );
                }

                department.setDepartmentName(name);
            }

            if (dto.getIsActive() != null) {
                department.setIsActive(dto.getIsActive());
            }

            if (dto.getDepartmentName() == null && dto.getIsActive() == null) {
                return new ApiResponseDTO<>(
                        null,
                        "No fields provided to update",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            Department updated = repository.save(department);

            return new ApiResponseDTO<>(
                    mapper.toDto(updated),
                    "Department updated successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("updateDepartment unexpected error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }



    @Override
    public ApiResponseDTO<DepartmentResponseDTO> getById(Long id) {

        log.info("Fetching department by id={}", id);

        try {
            return repository.findById(id)
                    .filter(d -> Boolean.TRUE.equals(d.getIsActive()))
                    .map(d -> new ApiResponseDTO<>(
                            mapper.toDto(d),
                            "Department found",
                            HttpStatus.OK,
                            false
                    ))
                    .orElseGet(() -> new ApiResponseDTO<>(
                            null,
                            "Department not found",
                            HttpStatus.NOT_FOUND,
                            true
                    ));

        } catch (Exception e) {
            log.error("getById unexpected error, id={}", id, e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @CacheEvict(value = "departments", allEntries = true)
    @ActivityLoggable(
            action = "DELETE",
            module = "DEPARTMENT",
            description = "Department id {0} deleted successfully"
    )
    public ApiResponseDTO<String> deleteById(Long id) {

        log.info("Deactivating department, id={}", id);

        try {
            Department department = repository.findById(id).orElse(null);

            if (department == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Department not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.FALSE.equals(department.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Department is already inactive",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            department.setIsActive(false);
            repository.save(department);

            return new ApiResponseDTO<>(
                    null,
                    "Department deleted successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("deleteById unexpected error, id={}", id, e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @Cacheable("departments")
    public ApiResponseDTO<DepartmentResponseDTO> getAllDepartments() {

        log.info("Fetching all active departments");

        try {
            List<DepartmentResponseDTO> list = repository.findAll()
                    .stream()
                    .filter(d -> Boolean.TRUE.equals(d.getIsActive()))
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            if (list.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No departments found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            return new ApiResponseDTO<>(
                    list,
                    HttpStatus.OK,
                    "Departments fetched successfully",
                    false,
                    LocalDateTime.now()
            );

        } catch (Exception e) {
            log.error("getAllDepartments error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<PagedResponse<DepartmentResponseDTO>> searchDepartments(
            DepartmentSearchRequestDto dto) {

        try {
            Pageable pageable = PaginationUtil.pageable(
                    dto.getPage(),
                    dto.getSize(),
                    dto.getSortBy(),
                    dto.getSortDir()
            );

            Page<Department> pageResult =
                    repository.searchDepartments(
                            dto.getQuery(),
                            dto.getIsActive(),
                            pageable
                    );

            if (pageResult.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No departments found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            List<DepartmentResponseDTO> content = pageResult.getContent()
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            PagedResponse<DepartmentResponseDTO> pagedResponse =
                    new PagedResponse<>(
                            content,
                            pageResult.getNumber(),
                            pageResult.getTotalElements(),
                            pageResult.getTotalPages(),
                            pageResult.getSize(),
                            pageResult.isLast()
                    );

            return new ApiResponseDTO<>(
                    pagedResponse,
                    "Departments fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("searchDepartments error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void exportDepartmentsCsv(HttpServletResponse response) {

        log.info("Exporting active departments to CSV");

        try {
            List<DepartmentResponseDTO> departments = repository.findAll()
                    .stream()
                    .filter(d -> Boolean.TRUE.equals(d.getIsActive()))
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            DepartmentExcelGenerator.generateExcel(departments, response);

            log.info("Departments CSV export completed successfully, totalRecords={}",
                    departments.size());

        } catch (Exception e) {
            log.error("exportDepartmentsCsv unexpected error", e);
            throw new RuntimeException("Failed to export departments CSV", e);
        }
    }
}
