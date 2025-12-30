package com.sipl.ticket.controller;

import com.sipl.ticket.core.dto.request.ProductSubCategoryRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ProductSubCategoryDto;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/product-sub-categories")
@CrossOrigin("*")
@Api(tags = "Product Sub Category APIs")
public interface ProductSubCategoryController {

    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<ProductSubCategoryDto>> saveProductSubCategory(
            @RequestBody ProductSubCategoryRequestDto productSubCategoryRequestDto
    );

    @PutMapping("/update")
    ResponseEntity<ApiResponseDTO<ProductSubCategoryDto>> updateProductSubCategory(
            @RequestBody ProductSubCategoryRequestDto productSubCategoryRequestDto
    );

    @GetMapping("/get/{productSubCategoryId}")
    ResponseEntity<ApiResponseDTO<ProductSubCategoryDto>> getById(
            @PathVariable Long productSubCategoryId
    );

    @DeleteMapping("/delete/{productSubCategoryId}")
    ResponseEntity<ApiResponseDTO<String>> deleteById(
            @PathVariable Long productSubCategoryId
    );

    @GetMapping("/getAll")
    ResponseEntity<ApiResponseDTO<PagedResponse<ProductSubCategoryDto>>> getAllProductSubCategories();
}
