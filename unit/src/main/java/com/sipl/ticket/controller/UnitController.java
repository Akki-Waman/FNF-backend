package com.sipl.ticket.controller;

import com.sipl.ticket.core.dto.request.UnitRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.UnitResponseDto;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/units")
@CrossOrigin("*")
@Api(tags = "Units APIs")
public interface UnitController {

    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<UnitResponseDto>> saveUnit(
            @Valid @RequestBody UnitRequestDto unitRequestDto
    );

    @PostMapping("/update")
    ResponseEntity<ApiResponseDTO<UnitResponseDto>> updateUnit(
            @Valid @RequestBody UnitRequestDto unitRequestDto
    );

    @GetMapping("/get/{unitId}")
    ResponseEntity<ApiResponseDTO<UnitResponseDto>> getById(
            @PathVariable Long unitId
    );

    @DeleteMapping("/delete/{unitId}")
    ResponseEntity<ApiResponseDTO<String>> deleteById(
            @PathVariable Long unitId
    );
}
