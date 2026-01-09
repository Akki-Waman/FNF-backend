package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.CountryController;
import com.sipl.ticket.core.dto.request.CountryRequestDto;
import com.sipl.ticket.core.dto.request.CountrySearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CountryResponseDto;
import com.sipl.ticket.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CountryControllerImpl implements CountryController {

    private final CountryService countryService;

    @Override
    public ResponseEntity<ApiResponseDTO<CountryResponseDto>> createCountry(
            @Valid @RequestBody CountryRequestDto requestDto) {

        log.info("<<Start>>createCountry endpoint called<<Start>>");

        ApiResponseDTO<CountryResponseDto> response =
                countryService.createCountry(requestDto);

        log.info("<<End>>createCountry endpoint called<<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<CountryResponseDto>> updateCountry(
            @PathVariable("countryId") Long countryId,
            @Valid @RequestBody CountryRequestDto requestDto) {

        log.info("<<Start>>updateCountry endpoint called<<Start>>");

        ApiResponseDTO<CountryResponseDto> response =
                countryService.updateCountry(countryId, requestDto);

        log.info("<<End>>updateCountry endpoint called<<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<CountryResponseDto>> getCountryById(
            Long countryId) {

        log.info("<<Start>>getCountryById endpoint called<<Start>>");

        ApiResponseDTO<CountryResponseDto> response =
                countryService.getCountryById(countryId);

        log.info("<<End>>getCountryById endpoint called<<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<CountryResponseDto>> getAllCountries() {

        log.info("<<Start>>getAllCountries endpoint called<<Start>>");

        ApiResponseDTO<CountryResponseDto> response =
                countryService.getAllCountries();

        log.info("<<End>>getAllCountries endpoint called<<End>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteCountry(
            Long countryId) {

        log.info("<<Start>>deleteCountry endpoint called<<Start>>");

        ApiResponseDTO<String> response =
                countryService.deleteCountry(countryId);

        log.info("<<End>>deleteCountry endpoint called<<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }





    @Override
    public ResponseEntity<ApiResponseDTO<Page<CountryResponseDto>>> searchCountries(
            @RequestBody CountrySearchRequestDto requestDto) {

        return countryService.searchCountries(requestDto);
    }



}
