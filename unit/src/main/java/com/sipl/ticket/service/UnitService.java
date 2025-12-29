package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.UnitRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.UnitResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface UnitService {

    ApiResponseDTO<UnitResponseDto> saveUnit(
            UnitRequestDto unitRequestDto
    );

    ApiResponseDTO<UnitResponseDto> updateUnit(
            UnitRequestDto unitRequestDto
    );

    ApiResponseDTO<UnitResponseDto> getById(
            Long unitId
    );

    ApiResponseDTO<String> deleteById(
            Long unitId
    );
}
