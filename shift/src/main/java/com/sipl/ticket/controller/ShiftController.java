package com.sipl.ticket.controller;

import com.sipl.ticket.core.dto.request.ShiftRequestDto;
import com.sipl.ticket.core.dto.request.ShiftSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ShiftResponseDTO;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/shifts")
@CrossOrigin("*")
@Api(tags = "Shift APIs")
public interface ShiftController {

    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<ShiftResponseDTO>> saveShift(
            @RequestBody ShiftRequestDto shiftRequestDto
    );

    @PostMapping("/update")
    ResponseEntity<ApiResponseDTO<ShiftResponseDTO>> updateShift(
            @RequestBody ShiftRequestDto shiftRequestDto
    );

    @GetMapping("/get/{shiftId}")
    ResponseEntity<ApiResponseDTO<ShiftResponseDTO>> getById(
            @PathVariable Long shiftId
    );

    @DeleteMapping("/delete/{shiftId}")
    ResponseEntity<ApiResponseDTO<String>> deleteById(
            @PathVariable Long shiftId
    );

    @GetMapping(" ")
    ResponseEntity<ApiResponseDTO<PagedResponse<ShiftResponseDTO>>> getAllShifts();

    @PostMapping("/search")
    ResponseEntity<ApiResponseDTO<PagedResponse<ShiftResponseDTO>>> searchShifts(
            @RequestBody ShiftSearchRequestDto requestDto
    );

}
