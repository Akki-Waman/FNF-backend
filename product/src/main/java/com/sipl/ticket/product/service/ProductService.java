package com.sipl.ticket.product.service;

import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CombinedProductResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ProductService {

    public ApiResponseDTO<CombinedProductResponseDto> saveOrUpdateProduct(
            Long productId, String productRequestDtoString, MultipartFile multipartFile);

}
