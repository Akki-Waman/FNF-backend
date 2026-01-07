package com.sipl.ticket.product.service;

import com.sipl.ticket.core.dto.request.ProductSearchRequestDto;
import com.sipl.ticket.core.dto.response.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ProductService {

    public ApiResponseDTO<CombinedProductResponseDto> saveOrUpdateProduct(
            Long productId, String productRequestDtoString, MultipartFile multipartFile);

    ApiResponseDTO<ProductDto> deleteProduct(Long productId);

    ApiResponseDTO<ProductDto> getByProduct(Long productId);

    ApiResponseDTO<ProductDto> getAllProduct();

    ApiResponseDTO<PagedResponse<ProductDto>>searchProducts(ProductSearchRequestDto requestDto);
}
