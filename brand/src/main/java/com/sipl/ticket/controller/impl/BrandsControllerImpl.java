package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.BrandsController;
import com.sipl.ticket.core.dto.request.BrandSearchRequestDto;
import com.sipl.ticket.core.dto.request.BrandsRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.BrandDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.service.BrandsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BrandsControllerImpl implements BrandsController {

    private final BrandsService brandsService;

    @Override
    public ResponseEntity<ApiResponseDTO<BrandDto>> saveBrand(
            @Valid @RequestBody BrandsRequestDto dto) {

        log.info("<<Start>>saveBrand endpoint called<<Start>>");

        ApiResponseDTO<BrandDto> response = brandsService.saveBrand(dto);

        log.info("<<End>>saveBrand endpoint called<<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<BrandDto>> updateBrand(
            BrandsRequestDto brandsRequestDto) {

        log.info("<<Start>>updateBrand endpoint called<<Start>>");

        ResponseEntity<ApiResponseDTO<BrandDto>> response =
                ResponseEntity.ok(brandsService.updateBrand(brandsRequestDto));

        log.info("<<End>>updateBrand endpoint called<<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<BrandDto>> getById(
            Long brandId) {

        log.info("<<Start>>getById endpoint called<<Start>>");

        ResponseEntity<ApiResponseDTO<BrandDto>> response =
                ResponseEntity.ok(brandsService.getById(brandId));

        log.info("<<End>>getById endpoint called<<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteById(
            Long brandId) {

        log.info("<<Start>>deleteById endpoint called<<Start>>");

        ResponseEntity<ApiResponseDTO<String>> response =
                ResponseEntity.ok(brandsService.deleteById(brandId));

        log.info("<<End>>deleteById endpoint called<<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<BrandDto>>> getAllBrands() {

        log.info("<<Start>>getAllBrands endpoint called<<Start>>");

        ResponseEntity<ApiResponseDTO<PagedResponse<BrandDto>>> response =
                ResponseEntity.ok(brandsService.getAllBrands());

        log.info("<<End>>getAllBrands endpoint called<<End>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<BrandDto>>> searchBrands(
            BrandSearchRequestDto requestDto) {

        log.info("Searching brands with request: {}", requestDto);

        ApiResponseDTO<PagedResponse<BrandDto>> response =
                brandsService.searchBrands(requestDto);

        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @Override
    public ResponseEntity<Void> exportBrandsCsv(HttpServletResponse response) {

        log.info("<<Start>> exportBrandsCsv endpoint called <<Start>>");

        brandsService.exportBrandsCsv(response);

        log.info("<<End>> exportBrandsCsv endpoint called <<End>>");

        return ResponseEntity.ok().build();
    }

}
