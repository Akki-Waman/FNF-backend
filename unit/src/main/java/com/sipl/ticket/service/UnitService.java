package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.UnitRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.UnitDto;

import java.util.List;

public interface UnitService {

    ApiResponseDTO<UnitDto> saveUnit(UnitRequestDto unitRequestDto);

    ApiResponseDTO<UnitDto> updateUnit(UnitRequestDto unitRequestDto);

    ApiResponseDTO<UnitDto> getById(Long unitId);

    ApiResponseDTO<List<UnitDto>> getAllUnits();

    ApiResponseDTO<String> deleteById(Long unitId);
}
