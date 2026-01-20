package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.UnitController;
import com.sipl.ticket.core.dto.request.UnitRequestDto;
import com.sipl.ticket.core.dto.request.UnitSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.UnitDto;
import com.sipl.ticket.service.UnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UnitControllerImpl implements UnitController {

    private final UnitService unitService;

    @Override
    public ResponseEntity<ApiResponseDTO<UnitDto>> getUnit(Long unitId) {

        log.info("<<Start>> getUnit <<Start>>");

        ApiResponseDTO<UnitDto> response =
                unitService.getUnitById(unitId);

        log.info("<<End>> getUnit <<End>>");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<List<UnitDto>>> getAllUnits() {

        log.info("<<Start>> getAllUnits <<Start>>");

        ApiResponseDTO<List<UnitDto>> response =
                unitService.getAllUnits();

        log.info("<<End>> getAllUnits <<End>>");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<UnitDto>> createUnit(
            @Valid @RequestBody UnitRequestDto requestDto) {

        log.info("<<Start>> createUnit <<Start>>");

        ApiResponseDTO<UnitDto> response =
                unitService.createUnit(requestDto);

        log.info("<<End>> createUnit <<End>>");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<UnitDto>> updateUnit(
            Long unitId, @Valid @RequestBody UnitRequestDto requestDto) {

        log.info("<<Start>> updateUnit <<Start>>");

        ApiResponseDTO<UnitDto> response =
                unitService.updateUnit(unitId, requestDto);

        log.info("<<End>> updateUnit <<End>>");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteUnit(Long unitId) {

        log.info("<<Start>> deleteUnit <<Start>>");

        ApiResponseDTO<String> response =
                unitService.deleteUnit(unitId);

        log.info("<<End>> deleteUnit <<End>>");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<Void> exportUnitsExcel(HttpServletResponse response) {

        log.info("<<Start>> exportUnitsExcel endpoint called <<Start>>");

        unitService.exportUnitsExcel(response);

        log.info("<<End>> exportUnitsExcel endpoint called <<End>>");

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<UnitDto>>>
    searchUnits(
            @RequestBody UnitSearchRequestDto requestDto) {

        log.info("<<Start>> searchUnits <<Start>>");

        ApiResponseDTO<PagedResponse<UnitDto>> response =
                unitService.searchUnits(requestDto);

        log.info("<<End>> searchUnits <<End>>");

        return ResponseEntity.ok(response);
    }

}
