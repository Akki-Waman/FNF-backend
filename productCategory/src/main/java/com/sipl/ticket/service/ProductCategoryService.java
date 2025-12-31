package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.ProductCategoryRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ProductCategoryDto;
import org.springframework.stereotype.Service;

@Service
public interface ProductCategoryService {

    ApiResponseDTO<ProductCategoryDto> saveProductCategory(
            ProductCategoryRequestDto productCategoryRequestDto
    );

    ApiResponseDTO<ProductCategoryDto> updateProductCategory(
            ProductCategoryRequestDto productCategoryRequestDto
    );

    ApiResponseDTO<ProductCategoryDto> getById(
            Long productCategoryId
    );

    ApiResponseDTO<String> deleteById(
            Long productCategoryId
    );

    ApiResponseDTO<PagedResponse<ProductCategoryDto>> getAllProductCategories();
}
