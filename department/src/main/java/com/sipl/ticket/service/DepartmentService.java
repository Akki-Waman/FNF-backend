package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.DepartmentRequestDto;
import com.sipl.ticket.core.dto.request.DepartmentSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.DepartmentResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public interface DepartmentService {

    public ApiResponseDTO<DepartmentResponseDTO> saveDepartment(
            DepartmentRequestDto departmentRequestDto
    );

    public ApiResponseDTO<DepartmentResponseDTO> updateDepartment(
            DepartmentRequestDto departmentRequestDto
    );

    public ApiResponseDTO<DepartmentResponseDTO> getById(
            Long departmentId
    );

    public ApiResponseDTO<String> deleteById(
            Long departmentId
    );

    public ApiResponseDTO<PagedResponse<DepartmentResponseDTO>> getAllDepartments();

    ApiResponseDTO<PagedResponse<DepartmentResponseDTO>> searchDepartments(
            DepartmentSearchRequestDto requestDto
    );

    void exportDepartmentsCsv(HttpServletResponse response);

}
