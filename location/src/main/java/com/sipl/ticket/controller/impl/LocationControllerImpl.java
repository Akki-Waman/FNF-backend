package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.LocationController;
import com.sipl.ticket.core.dto.request.LocationRequestDTO;
import com.sipl.ticket.core.dto.request.LocationSearchRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.LocationResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LocationControllerImpl implements LocationController {

    private final LocationService locationService;

    @Override
    public ResponseEntity<ApiResponseDTO<LocationResponseDTO>> saveLocation(
            @Valid @RequestBody LocationRequestDTO dto) {

        log.info("<<Start>>saveLocation endpoint called<<Start>>");

        ApiResponseDTO<LocationResponseDTO> response =
                locationService.saveLocation(dto);

        log.info("<<End>>saveLocation endpoint called<<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<LocationResponseDTO>> updateLocation(
            LocationRequestDTO locationRequestDTO) {

        log.info("<<Start>>updateLocation endpoint called<<Start>>");

        ResponseEntity<ApiResponseDTO<LocationResponseDTO>> response =
                ResponseEntity.ok(
                        locationService.updateLocation(locationRequestDTO)
                );

        log.info("<<End>>updateLocation endpoint called<<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<LocationResponseDTO>> getById(
            Long locationId) {

        log.info("<<Start>>getById endpoint called<<Start>>");

        ResponseEntity<ApiResponseDTO<LocationResponseDTO>> response =
                ResponseEntity.ok(
                        locationService.getById(locationId)
                );

        log.info("<<End>>getById endpoint called<<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteById(
            Long locationId) {

        log.info("<<Start>>deleteById endpoint called<<Start>>");

        ResponseEntity<ApiResponseDTO<String>> response =
                ResponseEntity.ok(
                        locationService.deleteById(locationId)
                );

        log.info("<<End>>deleteById endpoint called<<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<LocationResponseDTO>>> getAllLocations() {

        log.info("<<Start>>getAllLocations endpoint called<<Start>>");

        ResponseEntity<ApiResponseDTO<PagedResponse<LocationResponseDTO>>> response =
                ResponseEntity.ok(
                        locationService.getAllLocations()
                );

        log.info("<<End>>getAllLocations endpoint called<<End>>");
        return response;
    }
    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<LocationResponseDTO>>> searchLocations(
            LocationSearchRequestDTO requestDto) {

        log.info("Searching locations with request: {}", requestDto);

        ApiResponseDTO<PagedResponse<LocationResponseDTO>> response =
                locationService.searchLocations(requestDto);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
