package com.sipl.ticket.faq.controller;

import com.sipl.ticket.core.dto.request.FaqSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sipl.ticket.core.dto.response.FaqDto;
import io.swagger.annotations.Api;


@RestController
@RequestMapping("api/v1/faq")
@CrossOrigin("*")
@Api(tags = "FAQ Details APIs")
public interface FaqController {

    @PostMapping("/save")
    public ResponseEntity<ApiResponseDTO<FaqDto>> saveFaqDetails(
            @RequestPart("requestDto") String requestDto,
            @RequestPart(name = "attachment", required = false) MultipartFile attachment,
            @RequestPart(name = "image", required = false) MultipartFile image);

    @PostMapping("/update")
    public ResponseEntity<ApiResponseDTO<FaqDto>> updateFaqDetails(
            @RequestPart("requestDto") String requestDto,
            @RequestPart(name = "attachment", required = false) MultipartFile attachment,
            @RequestPart(name = "image", required = false) MultipartFile image);

    @PostMapping("/search")
    public ResponseEntity<ApiResponseDTO<PagedResponse<FaqDto>>> searchFaq(
            @RequestBody FaqSearchRequestDto faqSearchRequestDto);

    @GetMapping("/get/{faqId}")
    public ResponseEntity<ApiResponseDTO<FaqDto>> getById(@PathVariable Integer faqId);

    @DeleteMapping("/delete/{faqId}")
    public ResponseEntity<ApiResponseDTO<String>> deleteById(@PathVariable Integer faqId);

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponseDTO<FaqDto>> getAll();

    @PostMapping("/raiseQuestion")
    public ResponseEntity<ApiResponseDTO<FaqDto>> raiseQuestion(@RequestBody FaqDto faqDto);


}
