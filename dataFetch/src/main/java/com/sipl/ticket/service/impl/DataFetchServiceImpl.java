package com.sipl.ticket.service.impl;

import com.sipl.ticket.core.dao.entity.*;
import com.sipl.ticket.core.dao.repository.*;
import com.sipl.ticket.core.dto.response.*;
import com.sipl.ticket.core.mapper.*;
import com.sipl.ticket.service.DataFetchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataFetchServiceImpl implements DataFetchService {

    private final RegionRepository regionRepository;
    private final RegionMapper regionMapper;
    private final ZoneRepository zoneRepository;
    private final ZoneMapper zoneMapper;
    private final DivisionsRepository divisionsRepository;
    private final DivisionMapper divisionMapper;
    private final OperationalUnitRepository operationalUnitRepository;
    private final OperationalUnitMapper operationalUnitMapper;
    private final GstSlabRepository gstSlabMasterRepository;
    private final GstSlabMasterMapper gstSlabMasterMapper;

    @Value("${dms.service.url}")
    private String dmsBaseUrl;


    @Override
    public ApiResponseDTO<RegionResponseDTO> getAllRegions() {
        try {
            List<Region> regions = regionRepository.findByIsActiveTrue();
            List<RegionResponseDTO> regionDtos = regionMapper.mapRegionListToDtoList(regions);
            return new ApiResponseDTO<>(
                    null,
                    regionDtos,
                    null,
                    "Regions fetched successfully",
                    HttpStatus.OK,
                    false,
                    null,
                    null
            );

        } catch (Exception ex) {
            log.error("Error while fetching regions", ex);
            return new ApiResponseDTO<>(
                    null,
                    ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<ZoneResponseDTO> getAllZones() {
        try {
            List<Zone> zones = zoneRepository.findByIsActiveTrue();
            List<ZoneResponseDTO> zoneDtos = zoneMapper.mapZoneListToDtoList(zones);
            return new ApiResponseDTO<>(
                    null,
                    zoneDtos,
                    null,
                    "Zones fetched successfully",
                    HttpStatus.OK,
                    false,
                    null,
                    null
            );

        } catch (Exception ex) {
            log.error("Error while fetching Zones", ex);
            return new ApiResponseDTO<>(
                    null,
                    ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<DivisionResponseDTO> getAllDivisions() {
        try {
            List<Divisions> divisions = divisionsRepository.findByIsActiveTrue();
            List<DivisionResponseDTO> divisionDTOS = divisionMapper.mapDivisionListToDtoList(divisions);
            return new ApiResponseDTO<>(
                    null,
                    divisionDTOS,
                    null,
                    "Divisions fetched successfully",
                    HttpStatus.OK,
                    false,
                    null,
                    null
            );

        } catch (Exception ex) {
            log.error("Error while fetching Divisions", ex);
            return new ApiResponseDTO<>(
                    null,
                    ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<OperationalUnitResponseDTO> getAllgetAllOperationalUnits() {
        try {
            List<OperationalUnit> operationalUnits = operationalUnitRepository.findByIsActiveTrue();
            List<OperationalUnitResponseDTO> operationalUnitsDTOS = operationalUnitMapper.mapOpUnitListToDtoList(operationalUnits);
            return new ApiResponseDTO<>(
                    null,
                    operationalUnitsDTOS,
                    null,
                    "Operational Unit fetched successfully",
                    HttpStatus.OK,
                    false,
                    null,
                    null
            );

        } catch (Exception ex) {
            log.error("Error while fetching Operational Unit", ex);
            return new ApiResponseDTO<>(
                    null,
                    ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<ZoneResponseDTO> getAllZonesByRegions(Long regionId) {
        try {
            List<Zone> zones = zoneRepository.findByRegionId(regionId);
            if (zones.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No zones found for the given region",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }
            List<ZoneResponseDTO> zoneDtos = zoneMapper.mapZoneListToDtoList(zones);
            return new ApiResponseDTO<>(
                    null,
                    zoneDtos,
                    null,
                    "Zones fetched successfully",
                    HttpStatus.OK,
                    false,
                    null,
                    null
            );

        } catch (Exception ex) {
            log.error("Error while fetching Zones", ex);
            return new ApiResponseDTO<>(
                    null,
                    ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<DivisionResponseDTO> getAllDivisionsByZones(Long zoneId) {
        try {
            List<Divisions> divisions = divisionsRepository.findByZoneId(zoneId);
            if (divisions.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No divisions found for the given zone",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }
            List<DivisionResponseDTO> divisionDTOS = divisionMapper.mapDivisionListToDtoList(divisions);
            return new ApiResponseDTO<>(
                    null,
                    divisionDTOS,
                    null,
                    "Divisions fetched successfully",
                    HttpStatus.OK,
                    false,
                    null,
                    null
            );

        } catch (Exception ex) {
            log.error("Error while fetching Divisions", ex);
            return new ApiResponseDTO<>(
                    null,
                    ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<OperationalUnitResponseDTO> getAllOperationalUnitsByDivisions(Long divisionId) {
        try {
            List<OperationalUnit> operationalUnits = operationalUnitRepository.findByDivisionId(divisionId);
            if (operationalUnits.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No operational units found for the given division",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }
            List<OperationalUnitResponseDTO> operationalUnitsDTOS = operationalUnitMapper.mapOpUnitListToDtoList(operationalUnits);
            return new ApiResponseDTO<>(
                    null,
                    operationalUnitsDTOS,
                    null,
                    "Operational Unit fetched successfully",
                    HttpStatus.OK,
                    false,
                    null,
                    null
            );

        } catch (Exception ex) {
            log.error("Error while fetching Operational Unit", ex);
            return new ApiResponseDTO<>(
                    null,
                    ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }    }

    @Override
    public ApiResponseDTO<GstSlabDto> getAllGstSlabs() {
        try {
            List<GstSlabMaster> gstSlabs = gstSlabMasterRepository.findByIsActiveTrue();

            if (gstSlabs.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No GST slabs found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            List<GstSlabDto> gstSlabDtos =
                    gstSlabMasterMapper.mapGstSlabListToDtoList(gstSlabs);

            return new ApiResponseDTO<>(
                    null,
                    gstSlabDtos,
                    null,
                    "GST slabs fetched successfully",
                    HttpStatus.OK,
                    false,
                    null,
                    null
            );

        } catch (Exception ex) {
            log.error("Error while fetching GST slabs", ex);
            return new ApiResponseDTO<>(
                    null,
                    ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ResponseEntity<byte[]> getFileFromDms(Long documentId) {

        try {
            String dmsUrl = dmsBaseUrl + "/download/" + documentId;

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<byte[]> dmsResponse =
                    restTemplate.exchange(
                            dmsUrl,
                            HttpMethod.GET,
                            null,
                            byte[].class
                    );

            return ResponseEntity
                    .status(dmsResponse.getStatusCode())
                    .headers(dmsResponse.getHeaders())
                    .body(dmsResponse.getBody());

        } catch (Exception e) {
            log.error("Exception in downloadTaskFile", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

}
