package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.CityController;
import com.sipl.ticket.core.dto.request.CityRequestDto;
import com.sipl.ticket.core.dto.request.CitySearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CityResponseDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CityControllerImpl implements CityController {

    private final CityService cityService;

    @Override
    public ResponseEntity<ApiResponseDTO<CityResponseDto>> saveCity(
            CityRequestDto dto) {

        log.info("<<Start>> saveCity <<Start>>");

        ApiResponseDTO<CityResponseDto> response =
                cityService.saveCity(dto);

        log.info("<<End>> saveCity <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<CityResponseDto>> updateCity(
            CityRequestDto dto) {

        log.info("<<Start>> updateCity <<Start>>");

        ApiResponseDTO<CityResponseDto> response =
                cityService.updateCity(dto);

        log.info("<<End>> updateCity <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<CityResponseDto>> getById(
            Long cityId) {

        log.info("<<Start>> getCityById <<Start>>");

        ApiResponseDTO<CityResponseDto> response =
                cityService.getById(cityId);

        log.info("<<End>> getCityById <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteById(
            Long cityId) {

        log.info("<<Start>> deleteCity <<Start>>");

        ApiResponseDTO<String> response =
                cityService.deleteById(cityId);

        log.info("<<End>> deleteCity <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<CityResponseDto>> getAllCities() {

        log.info("<<Start>> getAllCities <<Start>>");

        ApiResponseDTO<CityResponseDto> response =
                cityService.getAllCities();

        log.info("<<End>> getAllCities <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<CityResponseDto>> getCitiesByStateId(
            Long stateId) {

        log.info("<<Start>> getCitiesByStateId <<Start>>");

        ApiResponseDTO<CityResponseDto> response =
                cityService.getCitiesByStateId(stateId);

        log.info("<<End>> getCitiesByStateId <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<CityResponseDto>>> searchCities(
            CitySearchRequestDto requestDto) {

        log.info("Searching cities with request: {}", requestDto);

        ApiResponseDTO<PagedResponse<CityResponseDto>> response =
                cityService.searchCities(requestDto);

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}