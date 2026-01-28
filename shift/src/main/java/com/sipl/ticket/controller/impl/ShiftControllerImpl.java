package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.ShiftController;
import com.sipl.ticket.core.dto.request.ShiftRequestDto;
import com.sipl.ticket.core.dto.request.ShiftSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ShiftResponseDTO;
import com.sipl.ticket.service.ShiftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
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

        return ResponseEntity.ok(response);
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
    public ResponseEntity<ApiResponseDTO<ShiftResponseDTO>> getAllShifts(Integer branchId) {

        log.info("<<Start>> getAllShifts called <<Start>>");

        ApiResponseDTO<ShiftResponseDTO> response =
                shiftService.getAllShifts(branchId);

        log.info("<<End>> getAllShifts endpoint called <<End>>");

        return ResponseEntity.ok(response);

    }


    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<ShiftResponseDTO>>> searchShifts(
            ShiftSearchRequestDto requestDto) {

        log.info("<<Start>>searchShifts endpoint called<<Start>>");

        ResponseEntity<ApiResponseDTO<PagedResponse<ShiftResponseDTO>>> response =
                ResponseEntity.ok(
                        shiftService.searchShifts(requestDto)
                );

        log.info("<<End>>searchShifts endpoint called<<End>>");

        return response;
    }

    @Override
    public void downloadExcel(HttpServletResponse response) {
        log.info("<<Start>> downloadExcel endpoint called <<Start>>");

        try {

            shiftService.downloadShiftsExcel(response);
            log.info("Excel download successfully generated");

        } catch (Exception e) {
            log.error("Error occurred while generating Excel", e);

            throw new RuntimeException("Failed to generate Excel file", e);
        }

        log.info("<<End>> downloadExcel endpoint called <<End>>");
    }

}

