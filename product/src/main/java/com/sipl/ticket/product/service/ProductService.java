package com.sipl.ticket.product.service;

import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CombinedProductResponseDto;
import com.sipl.ticket.core.dto.response.ProductDto;
import com.sipl.ticket.core.dto.response.ProductGetCustomDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ProductService {

    public ApiResponseDTO<CombinedProductResponseDto> saveOrUpdateProduct(
            Long productId, String productRequestDtoString, MultipartFile multipartFile);

    ApiResponseDTO<ProductDto> deleteProduct(Long productId);

    ApiResponseDTO<ProductDto> getByProduct(Long productId);

    ApiResponseDTO<ProductDto> getAllProduct();
}
