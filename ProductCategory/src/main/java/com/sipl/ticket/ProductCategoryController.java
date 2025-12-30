package com.sipl.ticket;

import com.sipl.ticket.core.dto.request.ProductCategoryRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ProductCategoryDto;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/product-categories")
@CrossOrigin("*")
@Api(tags = "Product Category APIs")
public interface ProductCategoryController {

    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<ProductCategoryDto>> saveProductCategory(
            @RequestBody ProductCategoryRequestDto productCategoryRequestDto
    );

    @PutMapping("/update")
    ResponseEntity<ApiResponseDTO<ProductCategoryDto>> updateProductCategory(
            @RequestBody ProductCategoryRequestDto productCategoryRequestDto
    );

    @GetMapping("/get/{productCategoryId}")
    ResponseEntity<ApiResponseDTO<ProductCategoryDto>> getById(
            @PathVariable Long productCategoryId
    );

    @DeleteMapping("/delete/{productCategoryId}")
    ResponseEntity<ApiResponseDTO<String>> deleteById(
            @PathVariable Long productCategoryId
    );

    @GetMapping("/getAll")
    ResponseEntity<ApiResponseDTO<PagedResponse<ProductCategoryDto>>> getAllProductCategories();
}
