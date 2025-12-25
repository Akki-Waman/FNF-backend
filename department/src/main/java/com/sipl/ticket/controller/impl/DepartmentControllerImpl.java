package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.DepartmentController;
import com.sipl.ticket.core.dto.request.DepartmentRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.DepartmentResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DepartmentControllerImpl implements DepartmentController {

    private final DepartmentService departmentService;

    @Override
    public ResponseEntity<ApiResponseDTO<DepartmentResponseDTO>> saveDepartment(
            DepartmentRequestDto departmentRequestDto) {

        log.info("DepartmentControllerImpl :: saveDepartment :: started");

        ResponseEntity<ApiResponseDTO<DepartmentResponseDTO>> response =
                ResponseEntity.ok(departmentService.saveDepartment(departmentRequestDto));

        log.info("DepartmentControllerImpl :: saveDepartment :: ended");

        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<DepartmentResponseDTO>> updateDepartment(
            DepartmentRequestDto departmentRequestDto) {

        log.info("DepartmentControllerImpl :: updateDepartment :: started");

        ResponseEntity<ApiResponseDTO<DepartmentResponseDTO>> response =
                ResponseEntity.ok(departmentService.updateDepartment(departmentRequestDto));

        log.info("DepartmentControllerImpl :: updateDepartment :: ended");

        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<DepartmentResponseDTO>> getById(
            Long departmentId) {

        log.info("DepartmentControllerImpl :: getById :: started");

        ResponseEntity<ApiResponseDTO<DepartmentResponseDTO>> response =
                ResponseEntity.ok(departmentService.getById(departmentId));

        log.info("DepartmentControllerImpl :: getById :: ended");

        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteById(
            Long departmentId) {

        log.info("DepartmentControllerImpl :: deleteById :: started");

        ResponseEntity<ApiResponseDTO<String>> response =
                ResponseEntity.ok(departmentService.deleteById(departmentId));

        log.info("DepartmentControllerImpl :: deleteById :: ended");

        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<DepartmentResponseDTO>>> getAllDepartments() {

        log.info("DepartmentControllerImpl :: getAllDepartments :: started");

        ResponseEntity<ApiResponseDTO<PagedResponse<DepartmentResponseDTO>>> response =
                ResponseEntity.ok(departmentService.getAllDepartments());

        log.info("DepartmentControllerImpl :: getAllDepartments :: ended");

        return response;
    }
}
