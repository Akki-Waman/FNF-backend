package com.sipl.ticket.controller;
import com.sipl.ticket.core.dto.request.SlaProfileRequestDto;

import com.sipl.ticket.core.dto.request.SlaProfileSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.SlaProfileResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/api/v1/sla-profiles")
@CrossOrigin("*")
@Api(tags = "SLA Profile APIs")
public interface SlaProfileController {

    @ApiOperation(
            value = "Create a new SLA Profile",
            response = SlaProfileResponseDto.class
    )
    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<SlaProfileResponseDto>> saveSlaProfile(
            @RequestBody SlaProfileRequestDto dto
    );

    @ApiOperation(
            value = "Update SLA Profile",
            response = SlaProfileResponseDto.class
    )
    @PutMapping("/update")
    ResponseEntity<ApiResponseDTO<SlaProfileResponseDto>> updateSlaProfile(
            @RequestBody SlaProfileRequestDto dto
    );

    @ApiOperation(
            value = "Get SLA Profile by ID",
            response = SlaProfileResponseDto.class
    )
    @GetMapping("/get/{slaProfileId}")
    ResponseEntity<ApiResponseDTO<SlaProfileResponseDto>> getById(
            @PathVariable Integer slaProfileId
    );

    @ApiOperation(
            value = "Delete SLA Profile",
            response = String.class
    )
    @DeleteMapping("/delete/{slaProfileId}")
    ResponseEntity<ApiResponseDTO<String>> deleteById(
            @PathVariable Integer slaProfileId
    );

    @ApiOperation(
            value = "Search SLA Profiles",
            response = SlaProfileResponseDto.class
    )
    @PostMapping("/search")
    ResponseEntity<ApiResponseDTO<PagedResponse<SlaProfileResponseDto>>> searchSlaProfiles(
            @RequestBody SlaProfileSearchRequestDto requestDto
    );

    @ApiOperation(
            value = "Get all SLA Profiles",
            notes = "Fetch all active SLA profiles",
            response = SlaProfileResponseDto.class
    )
    @GetMapping("")
    ResponseEntity<ApiResponseDTO<SlaProfileResponseDto>> getAllSlaProfiles();

    @PostMapping("/export")
    ResponseEntity<byte[]> exportSlaProfilesExcel(
            @RequestBody SlaProfileSearchRequestDto request);


}
