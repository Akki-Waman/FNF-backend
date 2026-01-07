package com.sipl.ticket.controller;

import com.sipl.ticket.core.dto.request.CountryRequestDto;
import com.sipl.ticket.core.dto.request.CountrySearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CompanyDto;
import com.sipl.ticket.core.dto.response.CountryResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/countries")
@CrossOrigin("*")
@Api(tags = "Country APIs")
public interface CountryController {

    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<CountryResponseDto>> createCountry(
            @RequestBody CountryRequestDto requestDto);

    @PutMapping("/update/{id}")
    ResponseEntity<ApiResponseDTO<CountryResponseDto>> updateCountry(
            @PathVariable Long id,
            @RequestBody CountryRequestDto requestDto);

    @ApiOperation(
            value = "Get Country by ID",
            notes = "Fetch Country details using Country ID",
            response = CountryResponseDto.class
    )
    @GetMapping("/get/{countryId}")
    ResponseEntity<ApiResponseDTO<CountryResponseDto>> getCountryById(
            @PathVariable("countryId") Long countryId);

    @ApiOperation(
            value = "Get All Countries",
            notes = "Fetch all active countries"
    )
    @GetMapping("")
    ResponseEntity<ApiResponseDTO<CountryResponseDto>> getAllCountries();

    @ApiOperation(
            value = "Delete Country",
            notes = "Soft deletes (deactivates) a country by ID"
    )
    @DeleteMapping("/delete/{countryId}")
    ResponseEntity<ApiResponseDTO<String>> deleteCountry(
            @PathVariable("countryId") Long countryId);

    @ApiOperation("Search Countries with filters & pagination")
    @PostMapping("/search")
    ResponseEntity<ApiResponseDTO<Page<CountryResponseDto>>> searchCountries(
            @RequestBody CountrySearchRequestDto requestDto);



}