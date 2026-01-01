package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.CountryRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CountryResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CountryService {

    ApiResponseDTO<CountryResponseDto> createCountry(
            CountryRequestDto requestDto
    );

    ApiResponseDTO<CountryResponseDto> updateCountry(
            Long countryId,
            CountryRequestDto requestDto
    );

    ApiResponseDTO<CountryResponseDto> getCountryById(
            Long countryId
    );

    ApiResponseDTO<List<CountryResponseDto>> getAllCountries();

    ApiResponseDTO<String> deleteCountry(
            Long countryId
    );
    /* -------- SEARCH -------- */
    ApiResponseDTO<List<CountryResponseDto>> searchCountries(
            String name,
            Boolean isForeign
    );
}
