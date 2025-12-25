package com.sipl.ticket.service.impl;

import com.sipl.ticket.core.dao.entity.Department;
import com.sipl.ticket.core.dao.repository.DepartmentRepository;
import com.sipl.ticket.core.dto.request.DepartmentRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.DepartmentResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.mapper.DepartmentMapper;
import com.sipl.ticket.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public ApiResponseDTO<DepartmentResponseDTO> saveDepartment(
            DepartmentRequestDto departmentRequestDto) {

        log.info("DepartmentServiceImpl :: saveDepartment :: started");

        try {
            if (departmentRequestDto == null ||
                    departmentRequestDto.getName() == null ||
                    departmentRequestDto.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Department name is required.");
            }

            Department department = departmentMapper.toEntity(departmentRequestDto);
            department.setIsActive(true);
            department.setIsDeleted(false);

            Department savedDepartment = departmentRepository.save(department);

            DepartmentResponseDTO responseDto =
                    departmentMapper.toResponseDto(savedDepartment);

            return new ApiResponseDTO<>(
                    responseDto,
                    "Department added successfully.",
                    HttpStatus.OK,
                    false
            );

        } catch (IllegalArgumentException ex) {
            return new ApiResponseDTO<>(
                    null,
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    true
            );
        } catch (Exception e) {
            log.error("Error occured while saveDepartment", e);
            return new ApiResponseDTO<>(
                    null,
                    "Error while adding Department. " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        } finally {
            log.info("DepartmentServiceImpl :: saveDepartment :: ended");
        }
    }

    @Override
    public ApiResponseDTO<DepartmentResponseDTO> updateDepartment(
            DepartmentRequestDto departmentRequestDto) {

        log.info("DepartmentServiceImpl :: updateDepartment :: started");

        try {
            if (departmentRequestDto == null || departmentRequestDto.getDepartmentId() == null) {
                throw new IllegalArgumentException("Department ID is required.");
            }

            Optional<Department> optionalDepartment =
                    departmentRepository.findById(departmentRequestDto.getDepartmentId());

            if (!optionalDepartment.isPresent()) {
                return new ApiResponseDTO<>(
                        null,
                        "Department data not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            Department department = optionalDepartment.get();
            department.setName(departmentRequestDto.getName());

            Department savedDepartment = departmentRepository.save(department);

            DepartmentResponseDTO responseDto =
                    departmentMapper.toResponseDto(savedDepartment);

            return new ApiResponseDTO<>(
                    responseDto,
                    "Department updated successfully.",
                    HttpStatus.OK,
                    false
            );

        } catch (IllegalArgumentException ex) {
            return new ApiResponseDTO<>(
                    null,
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    true
            );
        } catch (Exception e) {
            log.error("Error occured while updateDepartment", e);
            return new ApiResponseDTO<>(
                    null,
                    "Error while updating Department. " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        } finally {
            log.info("DepartmentServiceImpl :: updateDepartment :: ended");
        }
    }

    @Override
    public ApiResponseDTO<DepartmentResponseDTO> getById(Long departmentId) {

        log.info("DepartmentServiceImpl :: getById :: started");

        try {
            Optional<Department> departmentOpt =
                    departmentRepository.findById(departmentId);

            if (!departmentOpt.isPresent() ||
                    Boolean.TRUE.equals(departmentOpt.get().getIsDeleted())) {
                return new ApiResponseDTO<>(
                        null,
                        "Department data not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            DepartmentResponseDTO departmentDto =
                    departmentMapper.toResponseDto(departmentOpt.get());

            return new ApiResponseDTO<>(
                    departmentDto,
                    "Department Data Found",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("Error occured while getById", e);
            return new ApiResponseDTO<>(
                    null,
                    "Server Error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        } finally {
            log.info("DepartmentServiceImpl :: getById :: ended");
        }
    }

    @Override
    public ApiResponseDTO<String> deleteById(Long departmentId) {

        log.info("DepartmentServiceImpl :: deleteById :: started");

        try {
            Optional<Department> departmentOpt =
                    departmentRepository.findById(departmentId);

            if (!departmentOpt.isPresent()) {
                return new ApiResponseDTO<>(
                        null,
                        "Department data not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            Department department = departmentOpt.get();
            department.setIsActive(false);
            department.setIsDeleted(true);

            departmentRepository.save(department);

            return new ApiResponseDTO<>(
                    null,
                    "Department deleted successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("Error occured while deleteById", e);
            return new ApiResponseDTO<>(
                    null,
                    "Server Error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        } finally {
            log.info("DepartmentServiceImpl :: deleteById :: ended");
        }
    }

    @Override
    public ApiResponseDTO<PagedResponse<DepartmentResponseDTO>> getAllDepartments() {

        log.info("DepartmentServiceImpl :: getAllDepartments :: started");

        try {
            List<Department> departments =
                    departmentRepository.findByIsDeletedFalse();

            if (departments.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "Department data not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            List<DepartmentResponseDTO> departmentDtos =
                    departmentMapper.toResponseDtoList(departments);

            return new ApiResponseDTO<>(
                    new PagedResponse<>(
                            departmentDtos,
                            0,
                            departmentDtos.size(),
                            1,
                            departmentDtos.size(),
                            true
                    ),
                    "Department Data Found",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("Error occured while getAllDepartments", e);
            return new ApiResponseDTO<>(
                    null,
                    "Server Error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        } finally {
            log.info("DepartmentServiceImpl :: getAllDepartments :: ended");
        }
    }
}
