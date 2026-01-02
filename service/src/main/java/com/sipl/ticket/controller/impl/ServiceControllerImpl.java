package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.ServiceController;
import com.sipl.ticket.core.dto.request.ServiceRequestDto;
import com.sipl.ticket.core.dto.request.ServiceSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ServiceResponseDTO;
import com.sipl.ticket.service.ServiceService;
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
public class ServiceControllerImpl implements ServiceController {

    private final ServiceService serviceService;

    @Override
    public ResponseEntity<ApiResponseDTO<ServiceResponseDTO>> saveService(
            @Valid @RequestBody ServiceRequestDto dto) {

        log.info("<<Start>> saveService <<Start>>");

        ApiResponseDTO<ServiceResponseDTO> response =
                serviceService.saveService(dto);

        log.info("<<End>> saveService <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ServiceResponseDTO>> updateService(
            @RequestBody ServiceRequestDto dto) {

        log.info("<<Start>> updateService <<Start>>");

        ApiResponseDTO<ServiceResponseDTO> response =
                serviceService.updateService(dto);

        log.info("<<End>> updateService <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ServiceResponseDTO>> getById(
            Long serviceId) {

        log.info("<<Start>> getServiceById <<Start>>");

        ApiResponseDTO<ServiceResponseDTO> response =
                serviceService.getById(serviceId);

        log.info("<<End>> getServiceById <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteById(
            Long serviceId) {

        log.info("<<Start>> deleteService <<Start>>");

        ApiResponseDTO<String> response =
                serviceService.deleteById(serviceId);

        log.info("<<End>> deleteService <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ServiceResponseDTO>> getAllServices() {

        log.info("<<Start>> getAllServices <<Start>>");

        ApiResponseDTO<ServiceResponseDTO> response =
                serviceService.getAllServices();

        log.info("<<End>> getAllServices <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<Void> exportServicesExcel(
            HttpServletResponse response) {

        log.info("<<Start>> exportServicesExcel <<Start>>");

        serviceService.exportServicesExcel(response);

        log.info("<<End>> exportServicesExcel <<End>>");
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<ServiceResponseDTO>>> searchServices(
            @RequestBody ServiceSearchRequestDto requestDto) {

        log.info("Searching services with request: {}", requestDto);

        ApiResponseDTO<PagedResponse<ServiceResponseDTO>> response =
                serviceService.searchServices(requestDto);

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
