package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.UnitController;
import com.sipl.ticket.core.dto.request.UnitRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.UnitResponseDto;
import com.sipl.ticket.service.UnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UnitControllerImpl implements UnitController {

    private final UnitService unitService;

    @Override
    public ResponseEntity<ApiResponseDTO<UnitResponseDto>> saveUnit(
            @Valid @RequestBody UnitRequestDto unitRequestDto) {

        log.info("<<Start>> saveUnit endpoint called <<Start>>");
        ResponseEntity<ApiResponseDTO<UnitResponseDto>> response =
                ResponseEntity.ok(unitService.saveUnit(unitRequestDto));
        log.info("<<End>> saveUnit endpoint called <<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<UnitResponseDto>> updateUnit(
            @Valid @RequestBody UnitRequestDto unitRequestDto) {

        log.info("<<Start>> updateUnit endpoint called <<Start>>");
        ResponseEntity<ApiResponseDTO<UnitResponseDto>> response =
                ResponseEntity.ok(unitService.updateUnit(unitRequestDto));
        log.info("<<End>> updateUnit endpoint called <<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<UnitResponseDto>> getById(Long unitId) {

        log.info("<<Start>> getUnitById endpoint called <<Start>>");
        ResponseEntity<ApiResponseDTO<UnitResponseDto>> response =
                ResponseEntity.ok(unitService.getById(unitId));
        log.info("<<End>> getUnitById endpoint called <<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteById(Long unitId) {

        log.info("<<Start>> deleteUnitById endpoint called <<Start>>");
        ResponseEntity<ApiResponseDTO<String>> response =
                ResponseEntity.ok(unitService.deleteById(unitId));
        log.info("<<End>> deleteUnitById endpoint called <<End>>");
        return response;
    }
}
