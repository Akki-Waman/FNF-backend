package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.UnitRequestDto;
import com.sipl.ticket.core.dto.response.UnitDto;

import java.util.List;

public interface UnitService {

    UnitDto getUnitById(Long unitId);

    List<UnitDto> getAllUnits();

    UnitDto createUnit(UnitRequestDto requestDto);

    UnitDto updateUnit(Long unitId, UnitRequestDto requestDto);

    void deleteUnit(Long unitId);
}
