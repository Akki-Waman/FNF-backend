package com.sipl.ticket.controller;

import com.sipl.ticket.core.dto.request.UnitRequestDto;
import com.sipl.ticket.core.dto.response.UnitDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UnitController {

    ResponseEntity<UnitDto> getUnit(Long unitId);

    ResponseEntity<List<UnitDto>> getAllUnits();

    ResponseEntity<UnitDto> createUnit(UnitRequestDto requestDto);

    ResponseEntity<UnitDto> updateUnit(Long unitId, UnitRequestDto requestDto);

    ResponseEntity<Void> deleteUnit(Long unitId);
}
