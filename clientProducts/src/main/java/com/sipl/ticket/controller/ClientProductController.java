package com.sipl.ticket.controller;



import com.sipl.ticket.core.dto.request.ClientProductSearchRequestDto;
import com.sipl.ticket.core.dto.request.ClientProductsRequestDTO;
import com.sipl.ticket.core.dto.request.DepartmentRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ClientProductsResponseDTO;
import com.sipl.ticket.core.dto.response.DepartmentResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/client-products")
@CrossOrigin("*")
@Api(tags = "Client Product APIs")
public interface ClientProductController {

    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<ClientProductsResponseDTO>> saveClientProducts(
            @Valid @RequestBody ClientProductsRequestDTO clientProductsRequestDTO
    );

    @PutMapping("/update/{clientProductId}")
    ResponseEntity<ApiResponseDTO<ClientProductsResponseDTO>> updateClientProducts(
            @PathVariable Long clientProductId,
            @RequestBody ClientProductsRequestDTO clientProductsRequestDTO
    );

    @DeleteMapping("/{clientProductId}")
    ResponseEntity<ApiResponseDTO<ClientProductsResponseDTO>> deleteClientProducts(
            @PathVariable Long clientProductId
    );

    @GetMapping(" ")
    ResponseEntity<ApiResponseDTO<PagedResponse<ClientProductsResponseDTO>>> getAllClientProducts();

    @PostMapping("/search")
    ResponseEntity<ApiResponseDTO<PagedResponse<ClientProductsResponseDTO>>> searchClientProducts(
            @RequestBody ClientProductSearchRequestDto requestDto
    );

    @GetMapping("/export")
    ResponseEntity<Void> exportClientProductsExcel(HttpServletResponse response);

}