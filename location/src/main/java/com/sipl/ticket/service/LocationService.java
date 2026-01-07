package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.LocationRequestDTO;
import com.sipl.ticket.core.dto.request.LocationSearchRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.LocationResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import org.springframework.stereotype.Service;

@Service
public interface LocationService {

    /* ===================== SAVE ===================== */
    ApiResponseDTO<LocationResponseDTO> saveLocation(
            LocationRequestDTO locationRequestDTO
    );

    /* ===================== UPDATE ===================== */
    ApiResponseDTO<LocationResponseDTO> updateLocation(
            LocationRequestDTO locationRequestDTO
    );

    /* ===================== GET BY ID ===================== */
    ApiResponseDTO<LocationResponseDTO> getById(
            Long locationId
    );

    /* ===================== DELETE ===================== */
    ApiResponseDTO<String> deleteById(
            Long locationId
    );

    /* ===================== GET ALL ===================== */
    ApiResponseDTO<PagedResponse<LocationResponseDTO>> getAllLocations();

    ApiResponseDTO<PagedResponse<LocationResponseDTO>> searchLocations(
            LocationSearchRequestDTO requestDto
    );

}
