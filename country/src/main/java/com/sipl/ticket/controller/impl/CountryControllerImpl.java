package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.CountryController;
import com.sipl.ticket.core.dto.request.CountryRequestDto;
import com.sipl.ticket.core.dto.response.CountryResponseDto;
import com.sipl.ticket.service.CountryService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/countries")
@CrossOrigin("*")
@Api(tags = "Country APIs")
@RequiredArgsConstructor
@Slf4j
public class CountryControllerImpl implements CountryController {

    private final CountryService countryService;

    @PostMapping
    public ResponseEntity<CountryResponseDto> createCountry(
            @Valid @RequestBody CountryRequestDto requestDto) {

        log.info("REST request to create country");
        return ResponseEntity.ok(countryService.createCountry(requestDto));
    }

    @PutMapping("/{countryId}")
    public ResponseEntity<CountryResponseDto> updateCountry(
            @PathVariable Long countryId,
            @Valid @RequestBody CountryRequestDto requestDto) {

        log.info("REST request to update country");
        return ResponseEntity.ok(countryService.updateCountry(countryId, requestDto));
    }

    @GetMapping("/{countryId}")
    public ResponseEntity<CountryResponseDto> getCountryById(
            @PathVariable Long countryId) {

        log.info("REST request to get country by ID");
        return ResponseEntity.ok(countryService.getCountryById(countryId));
    }

    @GetMapping
    public ResponseEntity<List<CountryResponseDto>> getAllCountries() {

        log.info("REST request to get all countries");
        return ResponseEntity.ok(countryService.getAllCountries());
    }

    @DeleteMapping("/{countryId}")
    public ResponseEntity<Void> deleteCountry(
            @PathVariable Long countryId) {

        log.info("REST request to delete country");
        countryService.deleteCountry(countryId);
        return ResponseEntity.noContent().build();
    }
}
