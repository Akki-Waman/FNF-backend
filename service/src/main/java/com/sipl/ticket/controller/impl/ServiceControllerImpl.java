package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.ServiceController;
import com.sipl.ticket.core.dto.request.ServiceRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ServiceResponseDTO;
import com.sipl.ticket.service.ServiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ServiceControllerImpl implements ServiceController {

    private final ServiceService serviceService;

    @Override
    public ResponseEntity<ApiResponseDTO<ServiceResponseDTO>> saveService(
            @Valid @RequestBody ServiceRequestDto dto) {

        log.info("<<Start>>saveService endpoint called<<Start>>");

        ApiResponseDTO<ServiceResponseDTO> response = serviceService.saveService(dto);

        log.info("<<End>>saveService endpoint called<<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ServiceResponseDTO>> updateService(
            ServiceRequestDto serviceRequestDto) {

        log.info("<<Start>>updateService endpoint called<<Start>>");

        ResponseEntity<ApiResponseDTO<ServiceResponseDTO>> response =
                ResponseEntity.ok(serviceService.updateService(serviceRequestDto));

        log.info("<<End>>updateService endpoint called<<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ServiceResponseDTO>> getById(
            Long serviceId) {

        log.info("<<Start>>getById endpoint called<<Start>>");

        ResponseEntity<ApiResponseDTO<ServiceResponseDTO>> response =
                ResponseEntity.ok(serviceService.getById(serviceId));

        log.info("<<End>>getById endpoint called<<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteById(
            Long serviceId) {

        log.info("<<Start>>deleteById endpoint called<<Start>>");

        ResponseEntity<ApiResponseDTO<String>> response =
                ResponseEntity.ok(serviceService.deleteById(serviceId));

        log.info("<<End>>deleteById endpoint called<<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<ServiceResponseDTO>>> getAllServices() {

        log.info("<<Start>>getAllServices endpoint called<<Start>>");

        ResponseEntity<ApiResponseDTO<PagedResponse<ServiceResponseDTO>>> response =
                ResponseEntity.ok(serviceService.getAllServices());

        log.info("<<End>>getAllServices endpoint called<<End>>");
        return response;
    }
}
