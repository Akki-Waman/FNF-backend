package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.DepartmentController;
import com.sipl.ticket.core.dto.request.DepartmentRequestDto;
import com.sipl.ticket.core.dto.request.DepartmentSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.DepartmentResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DepartmentControllerImpl implements DepartmentController {

    private final DepartmentService departmentService;

    @Override
    public ResponseEntity<ApiResponseDTO<DepartmentResponseDTO>> saveDepartment(
          @Valid @RequestBody DepartmentRequestDto departmentRequestDto) {

        log.info("<<Start>>saveDepartment endpoint called<<Start>>");

        ResponseEntity<ApiResponseDTO<DepartmentResponseDTO>> response =
                ResponseEntity.ok(departmentService.saveDepartment(departmentRequestDto));

        log.info("<<End>>saveDepartment endpoint called<<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<DepartmentResponseDTO>> updateDepartment(
         @Valid @RequestBody   DepartmentRequestDto departmentRequestDto) {

        log.info("<<Start>>updateDepartment endpoint called<<Start>>");

        ResponseEntity<ApiResponseDTO<DepartmentResponseDTO>> response =
                ResponseEntity.ok(departmentService.updateDepartment(departmentRequestDto));

        log.info("<<End>>updateDepartment endpoint called<<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<DepartmentResponseDTO>> getById(
            Long departmentId) {

        log.info("<<Start>>getById endpoint called<<Start>>");

        ResponseEntity<ApiResponseDTO<DepartmentResponseDTO>> response =
                ResponseEntity.ok(departmentService.getById(departmentId));

        log.info("<<End>>getById endpoint called<<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteById(
            Long departmentId) {

        log.info("<<Start>>deleteById endpoint called<<Start>>");

        ResponseEntity<ApiResponseDTO<String>> response =
                ResponseEntity.ok(departmentService.deleteById(departmentId));

        log.info("<<End>>deleteById endpoint called<<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<DepartmentResponseDTO>> getAllDepartments() {

        log.info("<<Start>> getAllDepartments <<Start>>");

        ApiResponseDTO<DepartmentResponseDTO> response =
                departmentService.getAllDepartments();

        log.info("<<End>> getAllDepartments <<End>>");

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<DepartmentResponseDTO>>> searchDepartments(
            @Valid DepartmentSearchRequestDto requestDto) {

        log.info("Searching departments: {}", requestDto);

        ApiResponseDTO<PagedResponse<DepartmentResponseDTO>> response =
                departmentService.searchDepartments(requestDto);

        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @Override
    public ResponseEntity<Void> exportDepartmentsCsv(HttpServletResponse response) {

        log.info("<<Start>> exportDepartmentsCsv endpoint called <<Start>>");

        departmentService.exportDepartmentsCsv(response);

        log.info("<<End>> exportDepartmentsCsv endpoint called <<End>>");

        return ResponseEntity.ok().build();
    }
}
