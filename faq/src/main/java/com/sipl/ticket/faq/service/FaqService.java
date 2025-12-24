package com.sipl.ticket.faq.service;

import com.sipl.ticket.core.dto.request.FaqSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.FaqDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FaqService {

    public ApiResponseDTO<FaqDto> saveFaqDetails(String requestDto, MultipartFile attachment, MultipartFile image);

    public ApiResponseDTO<PagedResponse<FaqDto>> searchFaq(FaqSearchRequestDto faqSearchRequestDto);

    public ApiResponseDTO<FaqDto> getById(Integer faqId);

    public ApiResponseDTO<String> deleteById(Integer faqId);

    public ApiResponseDTO<FaqDto> getAll();

    public ApiResponseDTO<FaqDto> updateFaqDetails(String requestDto, MultipartFile attachment, MultipartFile image);

    public ApiResponseDTO<FaqDto> raiseQuestion(FaqDto faqDto);
}
