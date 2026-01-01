package com.sipl.ticket.controller;

import com.sipl.ticket.core.dto.request.CountryRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CountryResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
            @RequestBody CountryRequestDto requestDto
    );


    @PutMapping("/update/{countryId}")
    ResponseEntity<ApiResponseDTO<CountryResponseDto>> updateCountry(
            @PathVariable Long countryId,
            @RequestBody CountryRequestDto requestDto
    );

    @GetMapping("/get/{countryId}")
    ResponseEntity<ApiResponseDTO<CountryResponseDto>> getCountryById(
            @PathVariable Long countryId
    );

    @GetMapping("/getAll")
    ResponseEntity<ApiResponseDTO<List<CountryResponseDto>>> getAllCountries();

    @DeleteMapping("/delete/{countryId}")
    ResponseEntity<ApiResponseDTO<String>> deleteCountry(
            @PathVariable Long countryId
    );
    @GetMapping("/search")
    ResponseEntity<ApiResponseDTO<List<CountryResponseDto>>> searchCountries(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean isForeign
    );

}
