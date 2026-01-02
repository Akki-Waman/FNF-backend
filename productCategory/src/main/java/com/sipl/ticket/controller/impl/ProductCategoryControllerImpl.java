package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.ProductCategoryController;
import com.sipl.ticket.core.dto.request.ProductCategoryRequestDto;
import com.sipl.ticket.core.dto.request.ProductCategorySearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ProductCategoryDto;
import com.sipl.ticket.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductCategoryControllerImpl implements ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @Override
    public ResponseEntity<ApiResponseDTO<ProductCategoryDto>> saveProductCategory(
            @Valid @RequestBody ProductCategoryRequestDto dto) {

        log.info("<<Start>> saveProductCategory <<Start>>");

        ApiResponseDTO<ProductCategoryDto> response =
                productCategoryService.saveProductCategory(dto);

        log.info("<<End>> saveProductCategory <<End>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ProductCategoryDto>> updateProductCategory(
            @RequestBody ProductCategoryRequestDto dto) {

        log.info("<<Start>> updateProductCategory <<Start>>");

        ApiResponseDTO<ProductCategoryDto> response =
                productCategoryService.updateProductCategory(dto);

        log.info("<<End>> updateProductCategory <<End>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ProductCategoryDto>> getById(
            Long productCategoryId) {

        log.info("<<Start>> getById <<Start>>");

        ApiResponseDTO<ProductCategoryDto> response =
                productCategoryService.getById(productCategoryId);

        log.info("<<End>> getById <<End>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteById(
            Long productCategoryId) {

        log.info("<<Start>> deleteById <<Start>>");

        ApiResponseDTO<String> response =
                productCategoryService.deleteById(productCategoryId);

        log.info("<<End>> deleteById <<End>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ProductCategoryDto>> getAllProductCategories() {

        log.info("<<Start>> getAllProductCategories <<Start>>");

        ApiResponseDTO<ProductCategoryDto> response =
                productCategoryService.getAllProductCategories();

        log.info("<<End>> getAllProductCategories <<End>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<ProductCategoryDto>>>
    searchProductCategories(
            @RequestBody ProductCategorySearchRequestDto requestDto) {

        log.info("Searching product categories with request: {}", requestDto);

        ApiResponseDTO<PagedResponse<ProductCategoryDto>> response =
                productCategoryService.searchProductCategories(requestDto);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> exportProductCategoriesExcel(
            HttpServletResponse response) {

        log.info("<<Start>> exportProductCategoriesExcel <<Start>>");

        productCategoryService.exportProductCategoriesExcel(response);

        log.info("<<End>> exportProductCategoriesExcel <<End>>");
        return ResponseEntity.ok().build();
    }
}