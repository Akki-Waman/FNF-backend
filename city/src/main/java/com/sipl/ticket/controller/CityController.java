package com.sipl.ticket.controller;

import com.sipl.ticket.core.dto.request.CityRequestDto;
import com.sipl.ticket.core.dto.request.CitySearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CityResponseDto;
import com.sipl.ticket.core.dto.response.CitySearchResponseDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequestMapping("/api/v1/cities")
@RestController
@CrossOrigin(origins = "*")
@Api(tags = "City APIs")
public interface CityController {

    @ApiOperation(
            value = "Create a new city",
            notes = "Provide the necessary city information to save a new city",
            response = CityResponseDto.class
    )
    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<CityResponseDto>> saveCity(
            @RequestBody CityRequestDto cityRequestDto
    );

    @ApiOperation(
            value = "Update city details",
            notes = "Provide city ID and updated city information",
            response = CityResponseDto.class
    )
    @PutMapping("/update")
    ResponseEntity<ApiResponseDTO<CityResponseDto>> updateCity(
            @RequestBody CityRequestDto cityRequestDto
    );

    @ApiOperation(
            value = "Get city by ID",
            notes = "Fetch city details using city ID",
            response = CityResponseDto.class
    )
    @GetMapping("/get/{cityId}")
    ResponseEntity<ApiResponseDTO<CityResponseDto>> getById(
            @PathVariable Long cityId
    );

    @ApiOperation(
            value = "Delete city",
            notes = "Soft delete city by city ID",
            response = String.class
    )
    @DeleteMapping("/delete/{cityId}")
    ResponseEntity<ApiResponseDTO<String>> deleteById(
            @PathVariable Long cityId
    );

    @ApiOperation(
            value = "Get all cities",
            notes = "Fetch all active cities in descending order of cityId",
            response = CityResponseDto.class
    )
    @GetMapping("")
    ResponseEntity<ApiResponseDTO<CityResponseDto>> getAllCities();

    @ApiOperation(
            value = "Get cities by state ID",
            notes = "Fetch all active cities by state ID",
            response = CityResponseDto.class
    )
    @GetMapping("/state/{stateId}")
    ResponseEntity<ApiResponseDTO<CityResponseDto>> getCitiesByStateId(
            @PathVariable Long stateId
    );

    @ApiOperation(
            value = "Search cities",
            notes = "Search cities with pagination, sorting and filters",
            response = CityResponseDto.class
    )
    @PostMapping("/search")
    ResponseEntity<ApiResponseDTO<PagedResponse<CitySearchResponseDto>>> searchCities(
            @RequestBody CitySearchRequestDto requestDto
    );

    @GetMapping("/export")
    ResponseEntity<Void> exportCitiesExcel(HttpServletResponse response);

}