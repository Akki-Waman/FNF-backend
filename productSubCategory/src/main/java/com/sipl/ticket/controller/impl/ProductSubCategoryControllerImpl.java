package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.ProductSubCategoryController;
import com.sipl.ticket.core.dto.request.ProductSubCategoryRequestDto;
import com.sipl.ticket.core.dto.request.ProductSubCategorySearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ProductSubCategoryDto;
import com.sipl.ticket.service.ProductSubCategoryService;
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
public class ProductSubCategoryControllerImpl implements ProductSubCategoryController {

    private final ProductSubCategoryService productSubCategoryService;

    @Override
    public ResponseEntity<ApiResponseDTO<ProductSubCategoryDto>> saveProductSubCategory(
            @Valid ProductSubCategoryRequestDto dto) {

        log.info("<<Start>> saveProductSubCategory <<Start>>");

        ApiResponseDTO<ProductSubCategoryDto> response =
                productSubCategoryService.saveProductSubCategory(dto);

        log.info("<<End>> saveProductSubCategory <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ProductSubCategoryDto>> updateProductSubCategory(
            ProductSubCategoryRequestDto dto) {

        log.info("<<Start>> updateProductSubCategory <<Start>>");

        ApiResponseDTO<ProductSubCategoryDto> response =
                productSubCategoryService.updateProductSubCategory(dto);

        log.info("<<End>> updateProductSubCategory <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ProductSubCategoryDto>> getById(Long id) {

        return ResponseEntity.ok(productSubCategoryService.getById(id));
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteById(Long id) {

        return ResponseEntity.ok(productSubCategoryService.deleteById(id));
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<ProductSubCategoryDto>>>
    getAllProductSubCategories() {

        return ResponseEntity.ok(productSubCategoryService.getAllProductSubCategories());
    }
    @Override
    public ResponseEntity<Void> exportProductSubCategoriesExcel(
            HttpServletResponse response) {

        log.info(
                "<<Start>> exportProductSubCategoriesExcel endpoint called <<Start>>");

        productSubCategoryService
                .exportProductSubCategoriesExcel(response);

        log.info(
                "<<End>> exportProductSubCategoriesExcel endpoint called <<End>>");

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<ProductSubCategoryDto>>>
    searchProductSubCategories(
            @RequestBody ProductSubCategorySearchRequestDto requestDto) {

        log.info("Searching product sub categories: {}", requestDto);

        ApiResponseDTO<PagedResponse<ProductSubCategoryDto>> response =
                productSubCategoryService.searchProductSubCategories(requestDto);

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
