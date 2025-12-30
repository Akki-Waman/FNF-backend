package com.sipl.ticket.controller;

import com.sipl.ticket.core.dto.request.UnitRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.UnitDto;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/units")
@CrossOrigin("*")
@Api(tags = "Units APIs")
public interface UnitController {

    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<UnitDto>> saveUnit(
            @Valid @RequestBody UnitRequestDto unitRequestDto
    );

    @PostMapping("/update")
    ResponseEntity<ApiResponseDTO<UnitDto>> updateUnit(
            @Valid @RequestBody UnitRequestDto unitRequestDto
    );

    @GetMapping("/get/{unitId}")
    ResponseEntity<ApiResponseDTO<UnitDto>> getById(
            @PathVariable Long unitId
    );

    @GetMapping("/get-all")
    ResponseEntity<ApiResponseDTO<List<UnitDto>>> getAllUnits();

    @DeleteMapping("/delete/{unitId}")
    ResponseEntity<ApiResponseDTO<String>> deleteById(
            @PathVariable Long unitId
    );
}
