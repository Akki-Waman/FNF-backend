package com.sipl.ticket.controller;

import com.sipl.ticket.core.dto.request.BrandSearchRequestDto;
import com.sipl.ticket.core.dto.request.BrandsRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.BrandDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/v1/brands")
@CrossOrigin("*")
@Api(tags = "Brand APIs")
public interface BrandsController {

    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<BrandDto>> saveBrand(
            @RequestBody BrandsRequestDto brandsRequestDto
    );

    @PutMapping("/update")
    ResponseEntity<ApiResponseDTO<BrandDto>> updateBrand(
            @RequestBody BrandsRequestDto brandsRequestDto
    );

    @GetMapping("/get/{brandId}")
    ResponseEntity<ApiResponseDTO<BrandDto>> getById(
            @PathVariable Long brandId
    );

    @DeleteMapping("/delete/{brandId}")
    ResponseEntity<ApiResponseDTO<String>> deleteById(
            @PathVariable Long brandId
    );

    @ApiOperation(
            value = "Get all brands",
            notes = "Fetch all active brands",
            response = BrandDto.class
    )
    @GetMapping("")
    ResponseEntity<ApiResponseDTO<BrandDto>> getAllBrands();

    @ApiOperation(
            value = "Search brands",
            notes = "Search brands with pagination, sorting and filters",
            response = BrandDto.class
    )
    @PostMapping("/search")
    ResponseEntity<ApiResponseDTO<PagedResponse<BrandDto>>> searchBrands(
            @RequestBody BrandSearchRequestDto requestDto
    );

    @GetMapping("/export")
    ResponseEntity<Void> exportBrandsCsv(HttpServletResponse response);

}
