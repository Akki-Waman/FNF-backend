package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.CountryRequestDto;
import com.sipl.ticket.core.dto.request.CountrySearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CountryResponseDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CountryService {

    ApiResponseDTO<CountryResponseDto> createCountry(CountryRequestDto requestDto);

    ApiResponseDTO<CountryResponseDto> updateCountry(Long countryId, CountryRequestDto requestDto);

    ApiResponseDTO<CountryResponseDto> getCountryById(Long countryId);

    ApiResponseDTO<CountryResponseDto> getAllCountries();

    ApiResponseDTO<String> deleteCountry(Long countryId);

    ResponseEntity<ApiResponseDTO<Page<CountryResponseDto>>> searchCountries(
            CountrySearchRequestDto requestDto);
    void exportCountriesExcel(HttpServletResponse response);

}
