package com.sipl.ticket.controller;

import com.sipl.ticket.core.dto.request.ShiftRequestDto;
import com.sipl.ticket.core.dto.request.ShiftSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.BrandDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ShiftResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/v1/shifts")
@CrossOrigin("*")
@Api(tags = "Shift APIs")
public interface ShiftController {

    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<ShiftResponseDTO>> saveShift(
            @RequestBody ShiftRequestDto shiftRequestDto
    );

    @PutMapping("/update")
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

    @ApiOperation(
            value = "Get all shifts",
            notes = "Fetch all active shifts",
            response = ShiftResponseDTO.class
    )
    @GetMapping("")
    ResponseEntity<ApiResponseDTO<ShiftResponseDTO>> getAllShifts(
            @RequestParam(required = false) Integer branchId
    );


    @PostMapping("/search")
    ResponseEntity<ApiResponseDTO<PagedResponse<ShiftResponseDTO>>> searchShifts(
            @RequestBody ShiftSearchRequestDto requestDto
    );
    @ApiOperation(
            value = "Export shifts to Excel",
            notes = "Download all active shifts in Excel format"
    )
    @GetMapping("/export")
    void downloadExcel(HttpServletResponse response);
}
