package com.sipl.ticket.faqCategory.controller;

import com.sipl.ticket.core.dto.request.FaqCategorySearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.FaqCategoryDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/faq-category")
@CrossOrigin(origins = "*")
@Api(tags = "Faq Category Details APIs")
public interface FaqCategoryController {
    @PostMapping("/save")
    public ResponseEntity<ApiResponseDTO<FaqCategoryDto>> saveFaqCategoryDetails(
            @RequestBody FaqCategoryDto FaqCategoryDto);

    @PostMapping("/update")
    public ResponseEntity<ApiResponseDTO<FaqCategoryDto>> updateFaqCategoryDetails(
            @RequestBody FaqCategoryDto FaqCategoryDto);

    @PostMapping("/search")
    public ResponseEntity<ApiResponseDTO<PagedResponse<FaqCategoryDto>>> searchFaqCategory(
            @RequestBody FaqCategorySearchRequestDto faqCategorySearchRequestDto);

    @GetMapping("/get/{faqCategoryId}")
    public ResponseEntity<ApiResponseDTO<FaqCategoryDto>> getById(@PathVariable Integer faqCategoryId);

    @DeleteMapping("/delete/{faqCategoryId}")
    public ResponseEntity<ApiResponseDTO<String>> deleteById(@PathVariable Integer faqCategoryId);

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponseDTO<FaqCategoryDto>> getAll();
}
