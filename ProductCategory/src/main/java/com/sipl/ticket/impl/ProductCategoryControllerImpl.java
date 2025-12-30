package com.sipl.ticket.impl;

import com.sipl.ticket.ProductCategoryController;
import com.sipl.ticket.core.dto.request.ProductCategoryRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ProductCategoryDto;
import com.sipl.ticket.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ProductCategoryDto>> updateProductCategory(
            @RequestBody ProductCategoryRequestDto dto) {

        log.info("<<Start>> updateProductCategory <<Start>>");

        return ResponseEntity.ok(
                productCategoryService.updateProductCategory(dto)
        );
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ProductCategoryDto>> getById(
            Long productCategoryId) {

        log.info("<<Start>> getById <<Start>>");

        return ResponseEntity.ok(
                productCategoryService.getById(productCategoryId)
        );
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteById(
            Long productCategoryId) {

        log.info("<<Start>> deleteById <<Start>>");

        return ResponseEntity.ok(
                productCategoryService.deleteById(productCategoryId)
        );
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<ProductCategoryDto>>> getAllProductCategories() {

        log.info("<<Start>> getAllProductCategories <<Start>>");

        return ResponseEntity.ok(
                productCategoryService.getAllProductCategories()
        );
    }
}
