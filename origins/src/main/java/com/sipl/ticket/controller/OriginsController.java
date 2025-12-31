package com.sipl.ticket.controller;

import com.sipl.ticket.core.dto.request.OriginsRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.OriginDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/v1/origins")
@CrossOrigin("*")
@Api(tags = "Origins APIs")
public interface OriginsController {

    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<OriginDto>> save(
            @RequestBody OriginsRequestDto dto
    );

    @PutMapping("/update")
    ResponseEntity<ApiResponseDTO<OriginDto>> update(
            @RequestBody OriginsRequestDto dto
    );

    @GetMapping("/get/{originId}")
    ResponseEntity<ApiResponseDTO<OriginDto>> getById(
            @PathVariable Long originId
    );

    @DeleteMapping("/delete/{originId}")
    ResponseEntity<ApiResponseDTO<String>> deleteById(
            @PathVariable Long originId
    );

    @GetMapping(" ")
    ResponseEntity<ApiResponseDTO<PagedResponse<OriginDto>>> getAll();

    @GetMapping("/export")
    void downloadExcel(HttpServletResponse response);
}

