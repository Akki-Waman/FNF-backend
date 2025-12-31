package com.sipl.ticket.controller;

import com.sipl.ticket.core.dto.request.UnitRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.UnitDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping("/api/v1/units")
@RestController
@CrossOrigin(origins = "*")
@Api(tags = "Unit APIs")
public interface UnitController {

    @ApiOperation(
            value = "Create a new unit",
            notes = "Provide unit details to create a new unit",
            response = UnitDto.class
    )
    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<UnitDto>> createUnit(
            @RequestBody UnitRequestDto requestDto
    );

    @ApiOperation(
            value = "Update unit details",
            notes = "Provide unit ID and updated unit information",
            response = UnitDto.class
    )
    @PutMapping("/update/{unitId}")
    ResponseEntity<ApiResponseDTO<UnitDto>> updateUnit(
            @PathVariable Long unitId,
            @RequestBody UnitRequestDto requestDto
    );

    @ApiOperation(
            value = "Get unit by ID",
            notes = "Fetch unit details using unit ID",
            response = UnitDto.class
    )
    @GetMapping("/get/{unitId}")
    ResponseEntity<ApiResponseDTO<UnitDto>> getUnit(
            @PathVariable Long unitId
    );

    @ApiOperation(
            value = "Delete unit",
            notes = "Soft delete unit by unit ID",
            response = String.class
    )
    @DeleteMapping("/delete/{unitId}")
    ResponseEntity<ApiResponseDTO<String>> deleteUnit(
            @PathVariable Long unitId
    );

    @ApiOperation(
            value = "Get all units",
            notes = "Fetch all active units",
            response = UnitDto.class
    )
    @GetMapping("")
    ResponseEntity<ApiResponseDTO<List<UnitDto>>> getAllUnits();

    @GetMapping("/export")
    ResponseEntity<Void> exportUnitsExcel(HttpServletResponse response);

}
