package com.sipl.ticket.product.controller;

import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CombinedProductResponseDto;
import com.sipl.ticket.core.dto.response.ProductDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1/products")
@RestController
@CrossOrigin(origins = "*")
@Api(tags = "Product APIs")
public interface ProductController {

    @ApiOperation(
            value = "Create a new product entry",
            notes = "Provide the necessary product information to save a new entry")
    @PostMapping("/save")
    public ResponseEntity<ApiResponseDTO<CombinedProductResponseDto>> addProduct(
            @RequestPart(value = "productRequestDto") String productRequestDto,
            @RequestPart(value = "image", required = false) MultipartFile multipartFile);

    @ApiOperation(
            value = "Update an existing product",
            notes = "Provide the necessary product information to save a new entry",
            response = ProductDto.class)
    @PutMapping("/update/{productId}")
    public ResponseEntity<ApiResponseDTO<CombinedProductResponseDto>> updateProduct(
            @PathVariable(value = "productId") Long productId,
            @RequestPart(value = "productRequestDto") String productRequestDto,
            @RequestPart(value = "image", required = false) MultipartFile multipartFile);

}
