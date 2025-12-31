package com.sipl.ticket.controller;

import com.sipl.ticket.core.dto.request.StateRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.StateResponseDto;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/states")
@CrossOrigin("*")
@Api(tags = "State APIs")
public interface StateController {

    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<StateResponseDto>> save(
            @RequestBody StateRequestDto dto
    );

    @PutMapping("/update")
    ResponseEntity<ApiResponseDTO<StateResponseDto>> update(
            @RequestBody StateRequestDto dto
    );

    @GetMapping("/get/{stateId}")
    ResponseEntity<ApiResponseDTO<StateResponseDto>> getById(
            @PathVariable Long stateId
    );

    @DeleteMapping("/delete/{stateId}")
    ResponseEntity<ApiResponseDTO<String>> deleteById(
            @PathVariable Long stateId
    );

    @GetMapping("/get-all")
    ResponseEntity<ApiResponseDTO<PagedResponse<StateResponseDto>>> getAll();
}

