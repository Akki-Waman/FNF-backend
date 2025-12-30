package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.UnitController;
import com.sipl.ticket.core.dto.request.UnitRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.UnitDto;
import com.sipl.ticket.service.UnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UnitControllerImpl implements UnitController {

    private final UnitService unitService;

    @Override
    public ResponseEntity<ApiResponseDTO<UnitDto>> saveUnit(
            @Valid UnitRequestDto unitRequestDto) {

        log.info("saveUnit API called");
        return ResponseEntity.ok(unitService.saveUnit(unitRequestDto));
    }

    @Override
    public ResponseEntity<ApiResponseDTO<UnitDto>> updateUnit(
            @Valid UnitRequestDto unitRequestDto) {

        log.info("updateUnit API called");
        return ResponseEntity.ok(unitService.updateUnit(unitRequestDto));
    }

    @Override
    public ResponseEntity<ApiResponseDTO<UnitDto>> getById(Long unitId) {

        log.info("getById API called");
        return ResponseEntity.ok(unitService.getById(unitId));
    }

    @Override
    public ResponseEntity<ApiResponseDTO<List<UnitDto>>> getAllUnits() {

        log.info("getAllUnits API called");
        return ResponseEntity.ok(unitService.getAllUnits());
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteById(Long unitId) {

        log.info("deleteById API called");
        return ResponseEntity.ok(unitService.deleteById(unitId));
    }
}
