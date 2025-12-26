package com.sipl.ticket.controller;


import com.sipl.ticket.core.dto.request.DepartmentRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.DepartmentResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/departments")
@CrossOrigin("*")
@Api(tags = "Department APIs")
public interface DepartmentController {

    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<DepartmentResponseDTO>> saveDepartment(
            @RequestBody DepartmentRequestDto departmentRequestDto
    );

    @PostMapping("/update")
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

    @GetMapping("/getAll")
    ResponseEntity<ApiResponseDTO<PagedResponse<DepartmentResponseDTO>>> getAllDepartments();
}
