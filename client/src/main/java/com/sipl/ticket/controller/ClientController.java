package com.sipl.ticket.controller;

import com.sipl.ticket.core.dto.request.ClientRequestDto;
import com.sipl.ticket.core.dto.request.ClientRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ClientResponseDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.SearchClientRequestDto;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/clients")
@CrossOrigin("*")
@Api(tags = "Client APIs")
public interface ClientController {


    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<ClientResponseDto>> saveClient(
            @RequestBody ClientRequestDto clientRequestDto
    );

    @PostMapping("/update")
    ResponseEntity<ApiResponseDTO<ClientResponseDto>> updateClient(
            @RequestBody ClientRequestDto clientRequestDto
    );

    @PostMapping("/search")
    ResponseEntity<ApiResponseDTO<PagedResponse<ClientResponseDto>>> searchClient(
            @RequestBody SearchClientRequestDto clientRequestDto
    );


    @GetMapping("/get/{clientId}")
    ResponseEntity<ApiResponseDTO<ClientResponseDto>> getById(
            @PathVariable Long clientId
    );

    @DeleteMapping("/delete/{clientId}")
    ResponseEntity<ApiResponseDTO<String>> deleteById(
            @PathVariable Long clientId
    );

    @GetMapping("/getAll")
    ResponseEntity<ApiResponseDTO<PagedResponse<ClientResponseDto>>> getAllClients();
}

