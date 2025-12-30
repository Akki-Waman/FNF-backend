package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.UnitRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.UnitDto;

public interface UnitService {

    ApiResponseDTO<UnitDto> saveUnit(UnitRequestDto dto);

    ApiResponseDTO<UnitDto> updateUnit(UnitRequestDto dto);

    ApiResponseDTO<UnitDto> getById(Long unitId);

    ApiResponseDTO<String> deleteById(Long unitId);
}
