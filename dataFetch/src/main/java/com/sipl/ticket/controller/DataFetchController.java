package com.sipl.ticket.controller;

import com.sipl.ticket.core.dao.entity.Zone;
import com.sipl.ticket.core.dto.response.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/data-fetch")
@CrossOrigin("*")
@Api(tags = "Data Fetch APIs")
public interface DataFetchController {
    @GetMapping("/get-regions")
    ResponseEntity<ApiResponseDTO<RegionResponseDTO>> getAllRegions();

    @GetMapping("/get-zones")
    ResponseEntity<ApiResponseDTO<ZoneResponseDTO>> getAllZones();

    @GetMapping("/get-zones/{regionId}")
    ResponseEntity<ApiResponseDTO<ZoneResponseDTO>> getAllZonesByRegions( @PathVariable Long regionId);

    @GetMapping("/get-divisions")
    ResponseEntity<ApiResponseDTO<DivisionResponseDTO>> getAllDivisions();

    @GetMapping("/get-divisions/{zoneId}")
    ResponseEntity<ApiResponseDTO<DivisionResponseDTO>> getAllDivisionsByZones(@PathVariable Long zoneId);

    @GetMapping("/get-operational-units")
    ResponseEntity<ApiResponseDTO<OperationalUnitResponseDTO>> getAllOperationalUnits();

    @GetMapping("/get-operational-units/{divisionId}")
    ResponseEntity<ApiResponseDTO<OperationalUnitResponseDTO>> getAllOperationalUnitsByDivisions(@PathVariable Long divisionId);

    @GetMapping("/get-gst-slabs")
    ResponseEntity<ApiResponseDTO<GstSlabDto>> getAllGstSlabs();

    @ApiOperation(
            value = "Download document.",
            notes = "Downloads document by document id"
    )
    @GetMapping("/files/{documentId}")
    public ResponseEntity<byte[]> getFileFromDms(@PathVariable Long documentId);

}
