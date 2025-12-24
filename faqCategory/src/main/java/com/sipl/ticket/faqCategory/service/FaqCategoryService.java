package com.sipl.ticket.faqCategory.service;

import com.sipl.ticket.core.dto.request.FaqCategorySearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.FaqCategoryDto;
import com.sipl.ticket.core.dto.response.PagedResponse;

public interface FaqCategoryService {
    
    ApiResponseDTO<FaqCategoryDto> saveFaqCategoryDetails(FaqCategoryDto faqCategoryDto);

    ApiResponseDTO<PagedResponse<FaqCategoryDto>> searchFaqCategory(FaqCategorySearchRequestDto faqCategorySearchRequestDto);

    ApiResponseDTO<FaqCategoryDto> getById(Integer faqCategoryId);

    ApiResponseDTO<String> deleteById(Integer faqCategoryId);

    ApiResponseDTO<FaqCategoryDto> getAll();

    ApiResponseDTO<FaqCategoryDto> updateFaqCategoryDetails(FaqCategoryDto faqCategoryDto);
}
