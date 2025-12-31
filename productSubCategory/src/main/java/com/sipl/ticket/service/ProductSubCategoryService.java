package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.ProductSubCategoryRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ProductSubCategoryDto;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public interface ProductSubCategoryService {

    ApiResponseDTO<ProductSubCategoryDto> saveProductSubCategory(
            ProductSubCategoryRequestDto requestDto
    );

    ApiResponseDTO<ProductSubCategoryDto> updateProductSubCategory(
            ProductSubCategoryRequestDto requestDto
    );

    ApiResponseDTO<ProductSubCategoryDto> getById(Long productSubCategoryId);

    ApiResponseDTO<String> deleteById(Long productSubCategoryId);

    ApiResponseDTO<PagedResponse<ProductSubCategoryDto>> getAllProductSubCategories();

    void exportProductSubCategoriesExcel(HttpServletResponse response);

}
