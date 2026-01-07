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
            @Valid @RequestBody ProductSubCategoryRequestDto dto) {

        log.info("<<Start>> saveProductSubCategory <<Start>>");

        ApiResponseDTO<ProductSubCategoryDto> response =
                productSubCategoryService.saveProductSubCategory(dto);

        log.info("<<End>> saveProductSubCategory <<End>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ProductSubCategoryDto>> updateProductSubCategory(
            @RequestBody ProductSubCategoryRequestDto dto) {

        log.info("<<Start>> updateProductSubCategory <<Start>>");

        ApiResponseDTO<ProductSubCategoryDto> response =
                productSubCategoryService.updateProductSubCategory(dto);

        log.info("<<End>> updateProductSubCategory <<End>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ProductSubCategoryDto>> getById(
            Long productSubCategoryId) {

        log.info("<<Start>> getById <<Start>>");

        ApiResponseDTO<ProductSubCategoryDto> response =
                productSubCategoryService.getById(productSubCategoryId);

        log.info("<<End>> getById <<End>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteById(
            Long productSubCategoryId) {

        log.info("<<Start>> deleteById <<Start>>");

        ApiResponseDTO<String> response =
                productSubCategoryService.deleteById(productSubCategoryId);

        log.info("<<End>> deleteById <<End>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ProductSubCategoryDto>> getAllProductSubCategories() {

        log.info("<<Start>> getAllProductSubCategories <<Start>>");

        ApiResponseDTO<ProductSubCategoryDto> response =
                productSubCategoryService.getAllProductSubCategories();

        log.info("<<End>> getAllProductSubCategories <<End>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> exportProductSubCategoriesExcel(
            HttpServletResponse response) {

        log.info("<<Start>> exportProductSubCategoriesExcel <<Start>>");

        productSubCategoryService.exportProductSubCategoriesExcel(response);

        log.info("<<End>> exportProductSubCategoriesExcel <<End>>");
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<ProductSubCategoryDto>>>
    searchProductSubCategories(
            @RequestBody ProductSubCategorySearchRequestDto requestDto) {

        log.info("<<Start>> searchProductSubCategories <<Start>>");

        ApiResponseDTO<PagedResponse<ProductSubCategoryDto>> response =
                productSubCategoryService.searchProductSubCategories(requestDto);
        log.info("<<End>> searchProductSubCategories <<End>>");

        return ResponseEntity.ok(response);
    }
}

