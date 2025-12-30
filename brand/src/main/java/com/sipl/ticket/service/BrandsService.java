package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.BrandSearchRequestDto;
import com.sipl.ticket.core.dto.request.BrandsRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.BrandDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import org.springframework.stereotype.Service;

@Service
public interface BrandsService {

    ApiResponseDTO<BrandDto> saveBrand(
            BrandsRequestDto brandsRequestDto
    );

    ApiResponseDTO<BrandDto> updateBrand(
            BrandsRequestDto brandsRequestDto
    );

    ApiResponseDTO<BrandDto> getById(
            Long brandId
    );

    ApiResponseDTO<String> deleteById(
            Long brandId
    );

    ApiResponseDTO<PagedResponse<BrandDto>> getAllBrands();

    ApiResponseDTO<PagedResponse<BrandDto>> searchBrands(
            BrandSearchRequestDto requestDto
    );

}
