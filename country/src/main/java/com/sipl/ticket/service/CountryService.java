package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.CountryRequestDto;
import com.sipl.ticket.core.dto.response.CountryResponseDto;

import java.util.List;

public interface CountryService {

    CountryResponseDto createCountry(CountryRequestDto requestDto);

    CountryResponseDto updateCountry(Long countryId, CountryRequestDto requestDto);

    CountryResponseDto getCountryById(Long countryId);

    List<CountryResponseDto> getAllCountries();

    void deleteCountry(Long countryId);
}
