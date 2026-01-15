package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.response.*;
import org.springframework.stereotype.Service;

@Service
public interface DataFetchService {
    ApiResponseDTO<RegionResponseDTO> getAllRegions();

    ApiResponseDTO<ZoneResponseDTO> getAllZones();

    ApiResponseDTO<DivisionResponseDTO> getAllDivisions();

    ApiResponseDTO<OperationalUnitResponseDTO> getAllgetAllOperationalUnits();

    ApiResponseDTO<ZoneResponseDTO> getAllZonesByRegions(Long regionId);

    ApiResponseDTO<DivisionResponseDTO> getAllDivisionsByZones(Long zoneId);

    ApiResponseDTO<OperationalUnitResponseDTO> getAllOperationalUnitsByDivisions(Long divisionId);
}
