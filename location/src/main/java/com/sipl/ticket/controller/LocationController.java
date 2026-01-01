package com.sipl.ticket.controller;

import com.sipl.ticket.core.dto.request.LocationRequestDTO;
import com.sipl.ticket.core.dto.request.LocationSearchRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.LocationResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/locations")
@CrossOrigin("*")
@Api(tags = "Location APIs")
public interface LocationController {

    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<LocationResponseDTO>> saveLocation(
            @RequestBody LocationRequestDTO locationRequestDTO
    );

    @PutMapping("/update")
    ResponseEntity<ApiResponseDTO<LocationResponseDTO>> updateLocation(
            @RequestBody LocationRequestDTO locationRequestDTO
    );

    @GetMapping("/get/{locationId}")
    ResponseEntity<ApiResponseDTO<LocationResponseDTO>> getById(
            @PathVariable Long locationId
    );

    @DeleteMapping("/delete/{locationId}")
    ResponseEntity<ApiResponseDTO<String>> deleteById(
            @PathVariable Long locationId
    );

    @GetMapping("/getAll")
    ResponseEntity<ApiResponseDTO<PagedResponse<LocationResponseDTO>>> getAllLocations();

    @PostMapping("/search")
    ResponseEntity<ApiResponseDTO<PagedResponse<LocationResponseDTO>>> searchLocations(
            @RequestBody LocationSearchRequestDTO requestDto
    );

}
