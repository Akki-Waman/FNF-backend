package com.sipl.ticket.product.service;

import com.sipl.ticket.core.dto.request.ProductSearchRequestDto;
import com.sipl.ticket.core.dto.response.*;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Service
public interface ProductService {

    public ApiResponseDTO<CombinedProductResponseDto> saveOrUpdateProduct(
            Long productId, String productRequestDtoString, MultipartFile multipartFile);

    ApiResponseDTO<ProductDto> deleteProduct(Long productId);

    ApiResponseDTO<CombinedProductResponseDto> getByProduct(Long productId);

    ApiResponseDTO<ProductDto> getAllProduct();

    ApiResponseDTO<PagedResponse<CombinedProductResponseDto>>searchProducts(ProductSearchRequestDto requestDto);

    void exportProductsExcel(HttpServletResponse response,Integer branchId);

    ResponseEntity<Resource> downloadProductFile(String fileName);

    ApiResponseDTO<Void> processExcelFile(MultipartFile file);



}
