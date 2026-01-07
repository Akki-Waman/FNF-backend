package com.sipl.ticket.controller;


import com.sipl.ticket.core.dto.request.DepartmentRequestDto;
import com.sipl.ticket.core.dto.request.DepartmentSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.BrandDto;
import com.sipl.ticket.core.dto.response.DepartmentResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/v1/departments")
@CrossOrigin("*")
@Api(tags = "Department APIs")
public interface DepartmentController {

    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<DepartmentResponseDTO>> saveDepartment(
            @RequestBody DepartmentRequestDto departmentRequestDto
    );

    @PutMapping("/update")
    ResponseEntity<ApiResponseDTO<DepartmentResponseDTO>> updateDepartment(
            @RequestBody DepartmentRequestDto departmentRequestDto
    );

    @GetMapping("/get/{departmentId}")
    ResponseEntity<ApiResponseDTO<DepartmentResponseDTO>> getById(
            @PathVariable Long departmentId
    );

    @DeleteMapping("/delete/{departmentId}")
    ResponseEntity<ApiResponseDTO<String>> deleteById(
            @PathVariable Long departmentId
    );
    @ApiOperation(
            value = "Get all departments",
            notes = "Fetch all active departments",
            response = DepartmentResponseDTO.class
    )
    @GetMapping("")
    ResponseEntity<ApiResponseDTO<DepartmentResponseDTO>> getAllDepartments();

    @ApiOperation(
            value = "Search departments",
            notes = "Search departments with pagination and filters",
            response = DepartmentResponseDTO.class
    )
    @PostMapping("/search")
    ResponseEntity<ApiResponseDTO<PagedResponse<DepartmentResponseDTO>>> searchDepartments(
            @RequestBody DepartmentSearchRequestDto requestDto
    );
    @GetMapping("/export")
    ResponseEntity<Void> exportDepartmentsCsv(HttpServletResponse response);

}
