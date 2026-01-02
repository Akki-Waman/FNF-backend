package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.ServiceRequestDto;
import com.sipl.ticket.core.dto.request.ServiceSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ServiceResponseDTO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public interface ServiceService {

    ApiResponseDTO<ServiceResponseDTO> saveService(
            ServiceRequestDto serviceRequestDto
    );

    ApiResponseDTO<ServiceResponseDTO> updateService(
            ServiceRequestDto serviceRequestDto
    );

    ApiResponseDTO<ServiceResponseDTO> getById(
            Long serviceId
    );

    ApiResponseDTO<String> deleteById(
            Long serviceId
    );

    ApiResponseDTO<ServiceResponseDTO> getAllServices();

    void exportServicesExcel(HttpServletResponse response);

    ApiResponseDTO<PagedResponse<ServiceResponseDTO>> searchServices(
            ServiceSearchRequestDto requestDto
    );
}
