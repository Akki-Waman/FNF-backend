package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.DataFetchController;
import com.sipl.ticket.core.dto.response.*;
import com.sipl.ticket.service.DataFetchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DataFetchControllerImpl implements DataFetchController {

    private final DataFetchService dataFetchService;

    @Override
    public ResponseEntity<ApiResponseDTO<RegionResponseDTO>> getAllRegions() {
        log.info("<<Start>>getAllRegions endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<RegionResponseDTO>> responseEntity =
                new ResponseEntity<>(dataFetchService.getAllRegions(), HttpStatus.OK);
        log.info("<<End>>getAllRegions endpoint called<<End>>");
        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ZoneResponseDTO>> getAllZones() {
        log.info("<<Start>>getAllZones endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<ZoneResponseDTO>> responseEntity =
                new ResponseEntity<>(dataFetchService.getAllZones(), HttpStatus.OK);
        log.info("<<End>>getAllZones endpoint called<<End>>");
        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ZoneResponseDTO>> getAllZonesByRegions(Long regionId) {
        log.info("<<Start>>getAllZonesByRegions endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<ZoneResponseDTO>> responseEntity =
                new ResponseEntity<>(dataFetchService.getAllZonesByRegions(regionId), HttpStatus.OK);
        log.info("<<End>>getAllZonesByRegions endpoint called<<End>>");
        return responseEntity;    }

    @Override
    public ResponseEntity<ApiResponseDTO<DivisionResponseDTO>> getAllDivisions() {
        log.info("<<Start>>getAllDivisions endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<DivisionResponseDTO>> responseEntity =
                new ResponseEntity<>(dataFetchService.getAllDivisions(), HttpStatus.OK);
        log.info("<<End>>getAllDivisions endpoint called<<End>>");
        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<DivisionResponseDTO>> getAllDivisionsByZones(Long zoneId) {
        log.info("<<Start>>getAllDivisionsByZones endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<DivisionResponseDTO>> responseEntity =
                new ResponseEntity<>(dataFetchService.getAllDivisionsByZones(zoneId), HttpStatus.OK);
        log.info("<<End>>getAllDivisionsByZones endpoint called<<End>>");
        return responseEntity;    }

    @Override
    public ResponseEntity<ApiResponseDTO<OperationalUnitResponseDTO>> getAllOperationalUnits() {
        log.info("<<Start>>getAllOperationalUnits endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<OperationalUnitResponseDTO>> responseEntity =
                new ResponseEntity<>(dataFetchService.getAllgetAllOperationalUnits(), HttpStatus.OK);
        log.info("<<End>>getAllOperationalUnits endpoint called<<End>>");
        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<OperationalUnitResponseDTO>> getAllOperationalUnitsByDivisions(Long divisionId) {
        log.info("<<Start>>getAllOperationalUnitsByDivisions endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<OperationalUnitResponseDTO>> responseEntity =
                new ResponseEntity<>(dataFetchService.getAllOperationalUnitsByDivisions(divisionId), HttpStatus.OK);
        log.info("<<End>>getAllOperationalUnitsByDivisions endpoint called<<End>>");
        return responseEntity;
    }
}
