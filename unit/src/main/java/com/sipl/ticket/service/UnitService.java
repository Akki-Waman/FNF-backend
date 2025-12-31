package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.UnitRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.UnitDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UnitService {

    ApiResponseDTO<UnitDto> getUnitById(
            Long unitId
    );

    ApiResponseDTO<List<UnitDto>> getAllUnits();

    ApiResponseDTO<UnitDto> createUnit(
            UnitRequestDto requestDto
    );

    ApiResponseDTO<UnitDto> updateUnit(
            Long unitId,
            UnitRequestDto requestDto
    );

    ApiResponseDTO<String> deleteUnit(
            Long unitId
    );
}
