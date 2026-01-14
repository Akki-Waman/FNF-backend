package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.CityRequestDto;
import com.sipl.ticket.core.dto.request.CitySearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CityResponseDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public interface CityService {

    ApiResponseDTO<CityResponseDto> saveCity(
            CityRequestDto cityRequestDto
    );

    ApiResponseDTO<CityResponseDto> updateCity(
            CityRequestDto cityRequestDto
    );

    ApiResponseDTO<CityResponseDto> getById(
            Long cityId
    );

    ApiResponseDTO<String> deleteById(
            Long cityId
    );

    ApiResponseDTO<CityResponseDto> getAllCities();

    ApiResponseDTO<CityResponseDto> getCitiesByStateId(
            Long stateId
    );

    ApiResponseDTO<PagedResponse<CityResponseDto>> searchCities(
            CitySearchRequestDto requestDto
    );

    void exportCitiesExcel(HttpServletResponse response);

}