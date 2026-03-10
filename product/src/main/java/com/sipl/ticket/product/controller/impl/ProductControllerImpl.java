package com.sipl.ticket.product.controller.impl;

import com.sipl.ticket.core.dto.request.ProductSearchRequestDto;
import com.sipl.ticket.core.dto.response.*;
import com.sipl.ticket.product.controller.ProductController;
import com.sipl.ticket.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

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


    @Override
    public ResponseEntity<ApiResponseDTO<ProductDto>> deleteProduct(Long productId) {
        log.info("<<START>> deleteProduct called <<START>>");
        ApiResponseDTO<ProductDto> apiResponse = productService.deleteProduct(productId);
        ResponseEntity<ApiResponseDTO<ProductDto>> responseEntity =
                new ResponseEntity<>(apiResponse, HttpStatus.OK);
        log.info("<<END>> deleteProduct  <<END>>");
        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<CombinedProductResponseDto>> getByProduct(Long productId) {
        log.info("<<START>> getByProduct called <<START>>");
        ApiResponseDTO<CombinedProductResponseDto> apiResponse = productService.getByProduct(productId);
        ResponseEntity<ApiResponseDTO<CombinedProductResponseDto>> responseEntity =
                new ResponseEntity<>(apiResponse, HttpStatus.OK);
        log.info("<<END>> getByProduct  <<END>>");
        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<ProductDto>> getAllProduct() {
        log.info("<<START>> getAllProduct called <<START>>");
        ApiResponseDTO<ProductDto> apiResponse = productService.getAllProduct();
        ResponseEntity<ApiResponseDTO<ProductDto>> responseEntity =
                new ResponseEntity<>(apiResponse, HttpStatus.OK);
        log.info("<<END>> getAllProduct  <<END>>");
        return responseEntity;
    }
    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<CombinedProductResponseDto>>>
    searchProducts(@RequestBody ProductSearchRequestDto requestDto) {
        log.info("<<START>> searchProducts called <<START>>");
        ApiResponseDTO<PagedResponse<CombinedProductResponseDto>> response =
                productService.searchProducts(requestDto);
        log.info("<<START>> searchProducts called <<START>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> exportProductsExcel(HttpServletResponse response) {

        log.info("<<Start>> exportProductsExcel endpoint called <<Start>>");

        productService.exportProductsExcel(response);

        log.info("<<End>> exportProductsExcel endpoint called <<End>>");

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> downloadProductFile(String fileName) {
        log.info("<<START>> downloadProductFile <<START>>");

        ResponseEntity<?> response =
                productService.downloadProductFile(fileName);

        log.info("<<END>> downloadProductFile <<END>>");
        return response;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<Void>> uploadFile(MultipartFile file) {

        log.info("<<START>> uploadFile called <<START>>");

        productService.processExcelFile(file);

        ApiResponseDTO<Void> response =
                new ApiResponseDTO<>(
                        "Excel file processed successfully",
                        HttpStatus.OK,
                        false
                );

        log.info("<<END>> uploadFile <<END>>");
        return ResponseEntity.ok(response);
    }


}
