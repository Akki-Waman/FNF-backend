package com.sipl.ticket.controller;

import com.sipl.ticket.core.dto.request.CountryRequestDto;
import com.sipl.ticket.core.dto.response.CountryResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CountryController {

    ResponseEntity<CountryResponseDto> createCountry(CountryRequestDto requestDto);

    ResponseEntity<CountryResponseDto> updateCountry(Long countryId, CountryRequestDto requestDto);

    ResponseEntity<CountryResponseDto> getCountryById(Long countryId);

    ResponseEntity<List<CountryResponseDto>> getAllCountries();

    ResponseEntity<Void> deleteCountry(Long countryId);
}
