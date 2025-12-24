package com.sipl.ticket.faq.controller.impl;

import com.sipl.ticket.core.dto.request.FaqSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.FaqDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.faq.controller.FaqController;
import com.sipl.ticket.faq.service.FaqService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FaqControllerImpl implements FaqController {

    private final FaqService faqService;

    @Override
    public ResponseEntity<ApiResponseDTO<FaqDto>> saveFaqDetails(
            String requestDto, MultipartFile attachment, MultipartFile image) {
        return ResponseEntity.ok(new ApiResponseDTO<>(null, "Saved", null, false));
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<FaqDto>>> searchFaq(FaqSearchRequestDto faqSearchRequestDto) {
        log.info("<<Start>>searchFaq endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<PagedResponse<FaqDto>>> responseEntity =
                new ResponseEntity<>(faqService.searchFaq(faqSearchRequestDto), HttpStatus.OK);
        log.info("<<End>>searchFaq endpoint called<<End>>");
        return responseEntity;
    }

   

    @Override
    public ResponseEntity<ApiResponseDTO<FaqDto>> getById(Integer faqId) {
        log.info("<<Start>>getById endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<FaqDto>> responseEntity =
                new ResponseEntity<>(faqService.getById(faqId), HttpStatus.OK);
        log.info("<<End>>getById endpoint called<<End>>");
        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteById(Integer faqId) {
        log.info("<<Start>>deleteById endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<String>> responseEntity =
                new ResponseEntity<>(faqService.deleteById(faqId), HttpStatus.OK);
        log.info("<<End>>deleteById endpoint called<<End>>");
        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<FaqDto>> getAll() {
        log.info("<<Start>>getAll endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<FaqDto>> responseEntity =
                new ResponseEntity<>(faqService.getAll(), HttpStatus.OK);
        log.info("<<End>>getAll endpoint called<<End>>");
        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<FaqDto>> updateFaqDetails(
            String requestDto, MultipartFile attachment, MultipartFile image) {
        log.info("<<Start>>updateFaqDetails endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<FaqDto>> responseEntity =
                new ResponseEntity<>(
                        faqService.updateFaqDetails(requestDto, attachment, image), HttpStatus.OK);
        log.info("<<End>>updateFaqDetails endpoint called<<End>>");
        return responseEntity;
    }



    @Override
    public ResponseEntity<ApiResponseDTO<FaqDto>> raiseQuestion(FaqDto faqDto) {
        log.info("<<Start>>raiseQuestion endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<FaqDto>> responseEntity =
                new ResponseEntity<>(faqService.raiseQuestion(faqDto), HttpStatus.OK);
        log.info("<<End>>updateFaqDetails endpoint called<<End>>");
        return responseEntity;
    }
}
