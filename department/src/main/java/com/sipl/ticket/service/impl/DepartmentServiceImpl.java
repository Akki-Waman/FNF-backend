package com.sipl.ticket.service.impl;

import com.sipl.ticket.core.dao.entity.Department;
import com.sipl.ticket.core.dao.repository.DepartmentRepository;
import com.sipl.ticket.core.dto.request.DepartmentRequestDto;
import com.sipl.ticket.core.dto.request.DepartmentSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.DepartmentResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.helper.DepartmentExcelGenerator;
import com.sipl.ticket.core.mapper.DepartmentMapper;
import com.sipl.ticket.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ApiResponseDTO<DepartmentResponseDTO> saveDepartment(DepartmentRequestDto dto) {

        log.info("<<Start>>saveDepartment endpoint called<<Start>>");

        try {

            String departmentName = dto.getDepartmentName().trim();

            if (repository.existsByDepartmentNameIgnoreCaseAndIsDeletedFalse(departmentName)) {
                return new ApiResponseDTO<>(null,
                        "Department "+departmentName+" already exists",
                        HttpStatus.CONFLICT,
                        true);
            }

            Department department = mapper.toEntity(dto);
            department.setIsActive(true);
            department.setIsDeleted(false);

            Department saved = repository.save(department);

            return new ApiResponseDTO<>(
                    mapper.toResponseDto(saved),
                    "Department "+departmentName+" created successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("saveDepartment error", e);
            return new ApiResponseDTO<>(null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true);
        } finally {
            log.info("<<End>>saveDepartment endpoint called<<End>>");
        }
    }


    @Override
    @CacheEvict(value = "departments", allEntries = true)
    public ApiResponseDTO<DepartmentResponseDTO> updateDepartment(DepartmentRequestDto dto) {

        log.info("<<Start>>updateDepartment endpoint called<<Start>>");

        try {

            Department department = repository.findById(dto.getDepartmentId())
                    .filter(d -> !Boolean.TRUE.equals(d.getIsDeleted()))
                    .orElse(null);

            if (department == null) {
                return new ApiResponseDTO<>(null,
                        "Department "+department+" not found",
                        HttpStatus.NOT_FOUND,
                        true);
            }

            String departmentName = dto.getDepartmentName().trim();

            if (repository.existsByDepartmentNameIgnoreCaseAndDepartmentIdNotAndIsDeletedFalse(
                    departmentName, dto.getDepartmentId())) {
                return new ApiResponseDTO<>(null,
                        "Department with same name already exists",
                        HttpStatus.CONFLICT,
                        true);
            }

            department.setDepartmentName(departmentName);
            Department updated = repository.save(department);

            return new ApiResponseDTO<>(
                    mapper.toResponseDto(updated),
                    "Department "+departmentName+" updated successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("updateDepartment unexpected error", e);
            return new ApiResponseDTO<>(null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true);
        } finally {
            log.info("<<End>>updateDepartment endpoint called<<End>>");
        }
    }

    @Override
    public ApiResponseDTO<DepartmentResponseDTO> getById(Long id) {

        log.info("<<Start>>getById endpoint called<<Start>>");

        try {
            return repository.findById(id)
                    .filter(d -> !Boolean.TRUE.equals(d.getIsDeleted()))
                    .map(d -> new ApiResponseDTO<>(
                            mapper.toResponseDto(d),
                            "Department found",
                            HttpStatus.OK,
                            false))
                    .orElseGet(() -> new ApiResponseDTO<>(
                            null,
                            "Department not found",
                            HttpStatus.NOT_FOUND,
                            true));
        } catch (Exception e) {
            log.error("getById unexpected error", e);
            return new ApiResponseDTO<>(null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true);
        } finally {
            log.info("<<End>>getById endpoint called<<End>>");
        }
    }

    @Override
    @CacheEvict(value = "departments", allEntries = true)
    public ApiResponseDTO<String> deleteById(Long id) {

        log.info("<<Start>> deleteById endpoint called<<Start>>");

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

            if (Boolean.TRUE.equals(department.getIsDeleted())) {
                return new ApiResponseDTO<>(
                        null,
                        "Department already deleted",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            department.setIsActive(false);
            department.setIsDeleted(true);
            repository.save(department);

            return new ApiResponseDTO<>(
                    null,
                    "Department deleted successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("Error while deleting department, id={}", id, e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        } finally {
            log.info("<<End>> deleteById endpoint called<<End>>");
        }
    }


    @Override
    @Cacheable("departments")
    public ApiResponseDTO<DepartmentResponseDTO> getAllDepartments() {

        log.info("Fetching all active departments");

        try {
            List<Department> list = repository.findByIsDeletedFalse();

            if (list.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No departments found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            List<DepartmentResponseDTO> response =
                    mapper.toResponseDtoList(list);

            return new ApiResponseDTO<>(
                    response,
                    HttpStatus.OK,
                    "Departments fetched successfully",
                    false,
                    LocalDateTime.now()
            );

        } catch (Exception e) {
            log.error("getAllDepartments unexpected error", e);
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

        Sort sort = dto.getSortDir().equalsIgnoreCase("desc")
                ? Sort.by(dto.getSortBy()).ascending()
                : Sort.by(dto.getSortBy()).descending();

        Pageable pageable =
                PageRequest.of(dto.getPage(), dto.getSize(), sort);

        Page<Department> pageResult =
                repository.searchByDepartmentId(
                        dto.getDepartmentId(),
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
                .map(mapper::toResponseDto)
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
    }
    @Override
    public void exportDepartmentsCsv(HttpServletResponse response) {

        log.info("Exporting active departments to CSV");

        try {
            List<DepartmentResponseDTO> departments = repository.findAll()
                    .stream()
                    .filter(d -> Boolean.TRUE.equals(d.getIsActive()))
                    .map(mapper::toResponseDto)
                    .collect(Collectors.toList()); // Java 8 safe

            DepartmentExcelGenerator.generateCsv(departments, response);

            log.info("Departments CSV export completed successfully, totalRecords={}",
                    departments.size());

        } catch (Exception e) {
            log.error("exportDepartmentsCsv unexpected error", e);
            throw new RuntimeException("Failed to export departments CSV", e);
        }
    }

}
