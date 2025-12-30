package com.sipl.ticket.product.controller.impl;

import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CombinedProductResponseDto;
import com.sipl.ticket.product.controller.ProductController;
import com.sipl.ticket.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
public class ProductControllerImpl implements ProductController {

    @Autowired
    private ProductService productService;

    @Override
    public ResponseEntity<ApiResponseDTO<CombinedProductResponseDto>> addProduct(String productRequestDto, MultipartFile multipartFile) {
        log.info("<<START>> addProduct controller <<START>>");
        ApiResponseDTO<CombinedProductResponseDto> apiResponse =
                productService.saveOrUpdateProduct(null, productRequestDto, multipartFile);
        log.info("<<END>> addProduct controller <<END>>");
        return ResponseEntity.ok(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<CombinedProductResponseDto>> updateProduct(Long productId, String productRequestDto, MultipartFile multipartFile) {
        log.info("<<START>> updateProduct controller <<START>>");
        ApiResponseDTO<CombinedProductResponseDto> apiResponse =
                productService.saveOrUpdateProduct(productId, productRequestDto, multipartFile);
        log.info("<<END>> updateProduct controller <<END>>");
        return ResponseEntity.ok(apiResponse);
    }
}
