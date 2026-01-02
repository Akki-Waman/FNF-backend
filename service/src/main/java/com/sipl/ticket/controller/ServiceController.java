package com.sipl.ticket.controller;

import com.sipl.ticket.core.dto.request.ServiceRequestDto;
import com.sipl.ticket.core.dto.request.ServiceSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ServiceResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/services")
@CrossOrigin("*")
@Api(tags = "Service APIs")
public interface ServiceController {

    @ApiOperation(
            value = "Create a new service",
            response = ServiceResponseDTO.class
    )
    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<ServiceResponseDTO>> saveService(
            @RequestBody ServiceRequestDto serviceRequestDto
    );

    @ApiOperation(
            value = "Update service",
            response = ServiceResponseDTO.class
    )
    @PutMapping("/update")
    ResponseEntity<ApiResponseDTO<ServiceResponseDTO>> updateService(
            @RequestBody ServiceRequestDto serviceRequestDto
    );

    @ApiOperation(
            value = "Get service by ID",
            response = ServiceResponseDTO.class
    )
    @GetMapping("/get/{serviceId}")
    ResponseEntity<ApiResponseDTO<ServiceResponseDTO>> getById(
            @PathVariable Long serviceId
    );

    @ApiOperation(
            value = "Delete service",
            response = String.class
    )
    @DeleteMapping("/delete/{serviceId}")
    ResponseEntity<ApiResponseDTO<String>> deleteById(
            @PathVariable Long serviceId
    );

    @ApiOperation(
            value = "Get all services",
            notes = "Fetch all active services",
            response = ServiceResponseDTO.class
    )
    @GetMapping("")
    ResponseEntity<ApiResponseDTO<ServiceResponseDTO>> getAllServices();

    @ApiOperation(
            value = "Export services to Excel"
    )
    @GetMapping("/export")
    ResponseEntity<Void> exportServicesExcel(HttpServletResponse response);

    @ApiOperation(
            value = "Search services",
            response = ServiceResponseDTO.class
    )
    @PostMapping("/search")
    ResponseEntity<ApiResponseDTO<PagedResponse<ServiceResponseDTO>>> searchServices(
            @RequestBody ServiceSearchRequestDto requestDto
    );
}
