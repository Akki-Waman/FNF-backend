package com.sipl.ticket.controller;

import com.sipl.ticket.core.dto.request.ServiceRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ServiceResponseDTO;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/services")
@CrossOrigin("*")
@Api(tags = "Service APIs")
public interface ServiceController {

    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<ServiceResponseDTO>> saveService(
            @RequestBody ServiceRequestDto serviceRequestDto
    );

    @PostMapping("/update")
    ResponseEntity<ApiResponseDTO<ServiceResponseDTO>> updateService(
            @RequestBody ServiceRequestDto serviceRequestDto
    );

    @GetMapping("/get/{serviceId}")
    ResponseEntity<ApiResponseDTO<ServiceResponseDTO>> getById(
            @PathVariable Long serviceId
    );

    @DeleteMapping("/delete/{serviceId}")
    ResponseEntity<ApiResponseDTO<String>> deleteById(
            @PathVariable Long serviceId
    );

    @GetMapping("/getAll")
    ResponseEntity<ApiResponseDTO<PagedResponse<ServiceResponseDTO>>> getAllServices();
}
