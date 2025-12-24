package com.sipl.ticket.faqCategory.controller.Impl;

import com.sipl.ticket.core.dto.request.FaqCategorySearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.FaqCategoryDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.faqCategory.controller.FaqCategoryController;
import com.sipl.ticket.faqCategory.service.FaqCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FaqCategoryControllerImpl implements FaqCategoryController {

    private final FaqCategoryService FaqCategoryService;

    @Override
    public ResponseEntity<ApiResponseDTO<FaqCategoryDto>> saveFaqCategoryDetails(
            FaqCategoryDto FaqCategoryDto) {
        log.info("<<Start>>saveFaqCategoryDetails endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<FaqCategoryDto>> responseEntity =
                new ResponseEntity<>(
                        FaqCategoryService.saveFaqCategoryDetails(FaqCategoryDto), HttpStatus.OK);
        log.info("<<End>>saveFaqCategoryDetails endpoint called<<End>>");
        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<FaqCategoryDto>>> searchFaqCategory(
            FaqCategorySearchRequestDto FaqCategorySearchRequestDto) {
        log.info("<<Start>>searchFaqCategory endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<PagedResponse<FaqCategoryDto>>> responseEntity =
                new ResponseEntity<>(
                        FaqCategoryService.searchFaqCategory(FaqCategorySearchRequestDto), HttpStatus.OK);
        log.info("<<End>>searchFaqCategory endpoint called<<End>>");
        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<FaqCategoryDto>> getById(Integer faqCategoryId) {
        log.info("<<Start>>getById endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<FaqCategoryDto>> responseEntity =
                new ResponseEntity<>(FaqCategoryService.getById(faqCategoryId), HttpStatus.OK);
        log.info("<<End>>getById endpoint called<<End>>");
        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteById(Integer faqCategoryId) {
        log.info("<<Start>>deleteById endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<String>> responseEntity =
                new ResponseEntity<>(FaqCategoryService.deleteById(faqCategoryId), HttpStatus.OK);
        log.info("<<End>>deleteById endpoint called<<End>>");
        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<FaqCategoryDto>> getAll() {
        log.info("<<Start>>getAll endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<FaqCategoryDto>> responseEntity =
                new ResponseEntity<>(FaqCategoryService.getAll(), HttpStatus.OK);
        log.info("<<End>>getAll endpoint called<<End>>");
        return responseEntity;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<FaqCategoryDto>> updateFaqCategoryDetails(
            FaqCategoryDto FaqCategoryDto) {
        log.info("<<Start>>updateFaqCategoryDetails endpoint called<<Start>>");
        ResponseEntity<ApiResponseDTO<FaqCategoryDto>> responseEntity =
                new ResponseEntity<>(
                        FaqCategoryService.updateFaqCategoryDetails(FaqCategoryDto), HttpStatus.OK);
        log.info("<<End>>updateFaqCategoryDetails endpoint called<<End>>");
        return responseEntity;
    }
}
