package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.ShiftController;
import com.sipl.ticket.core.dto.request.ShiftRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ShiftResponseDTO;
import com.sipl.ticket.service.ShiftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ShiftControllerImpl implements ShiftController {

    private final ShiftService shiftService;

    @Override
    public ResponseEntity<ApiResponseDTO<ShiftResponseDTO>> saveShift(
            @Valid ShiftRequestDto shiftRequestDto) {

        log.info("<<Start>>saveShift endpoint called<<Start>>");
        ApiResponseDTO<ShiftResponseDTO> response = shiftService.saveShift(shiftRequestDto);
        log.info("<<End>>saveShift endpoint called<<End>>");

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ShiftResponseDTO>> updateShift(
            ShiftRequestDto shiftRequestDto) {

        log.info("<<Start>>updateShift endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<ShiftResponseDTO>> response =
                ResponseEntity.ok(shiftService.updateShift(shiftRequestDto));
        log.info("<<End>>updateShift endpoint called<<End>>");

        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ShiftResponseDTO>> getById(
            Long shiftId) {

        log.info("<<Start>>getShiftById endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<ShiftResponseDTO>> response =
                ResponseEntity.ok(shiftService.getById(shiftId));
        log.info("<<End>>getShiftById endpoint called<<End>>");

        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteById(
            Long shiftId) {

        log.info("<<Start>>deleteShift endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<String>> response =
                ResponseEntity.ok(shiftService.deleteById(shiftId));
        log.info("<<End>>deleteShift endpoint called<<End>>");

        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<ShiftResponseDTO>>> getAllShifts() {

        log.info("<<Start>>getAllShifts endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<PagedResponse<ShiftResponseDTO>>> response =
                ResponseEntity.ok(shiftService.getAllShifts());
        log.info("<<End>>getAllShifts endpoint called<<End>>");

        return response;
    }
}

