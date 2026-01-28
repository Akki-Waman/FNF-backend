package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.response.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface DataFetchService {
    ApiResponseDTO<RegionResponseDTO> getAllRegions(Long companyId);

    ApiResponseDTO<ZoneResponseDTO> getAllZones(Long companyId);

    ApiResponseDTO<DivisionResponseDTO> getAllDivisions(Long companyId);

    ApiResponseDTO<OperationalUnitResponseDTO> getAllgetAllOperationalUnits();

    ApiResponseDTO<ZoneResponseDTO> getAllZonesByRegions(Long regionId);

    ApiResponseDTO<DivisionResponseDTO> getAllDivisionsByZones(Long zoneId);

    ApiResponseDTO<OperationalUnitResponseDTO> getAllOperationalUnitsByDivisions(Long divisionId);

    ApiResponseDTO<GstSlabDto> getAllGstSlabs();

    ResponseEntity<byte[]> getFileFromDms(Long documentId);

}
