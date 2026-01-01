package com.sipl.ticket.controller;


import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CombinedProductResponseDto;
import com.sipl.ticket.core.dto.response.CombinedTicketResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/v1/tickets")
@RestController
@CrossOrigin(origins = "*")
@Api(tags = "Tickets APIs")
public interface TicketController {

    @ApiOperation(
            value = "Create a new ticket entry",
            notes = "Provide the necessary ticket information to save a new entry")
    @PostMapping("/save")
    public ResponseEntity<ApiResponseDTO<CombinedTicketResponseDto>> addTickets(
            @RequestPart(value = "ticketRequestDto") String ticketRequestDto,
            @RequestPart(value = "image", required = false) List<MultipartFile> multipartFile);
}
