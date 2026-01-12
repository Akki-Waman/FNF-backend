package com.sipl.ticket.controller;


import com.sipl.ticket.core.dto.request.DeleteTicketsRequestDTO;
import com.sipl.ticket.core.dto.request.ExportSearchRequestDTO;
import com.sipl.ticket.core.dto.request.TicketSearchRequestDto;
import com.sipl.ticket.core.dto.response.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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

    @ApiOperation(
            value = "Delete multiple tickets",
            notes = "Delete tickets by providing list of ticket IDs"
    )
    @DeleteMapping("/delete")
    ResponseEntity<ApiResponseDTO<Void>> deleteTickets(
            @RequestBody DeleteTicketsRequestDTO requestDTO
    );
    @PostMapping("/search")
    ResponseEntity<ApiResponseDTO<PagedResponse<TicketCombinedResponseDto>>> searchTickets(
            @RequestBody TicketSearchRequestDto requestDto
    );

    @ApiOperation(
            value = "Update a existing ticket entry",
            notes = "Provide the necessary ticket information to save a new entry")
    @PutMapping("/update/{ticketId}")
    public ResponseEntity<ApiResponseDTO<CombinedTicketResponseDto>> updateTickets(
            @PathVariable Long ticketId,
            @RequestPart(value = "ticketRequestDto") String ticketRequestDto,
            @RequestPart(value = "image", required = false) List<MultipartFile> multipartFile);


    @GetMapping("/ticket-summary}")
    ResponseEntity<ApiResponseDTO<SummaryKpiResponseDTO>> getTikctSummary();

    @ApiOperation("Export tickets in Excel / CSV / PDF")
    @PostMapping("/export")
    ResponseEntity<Void> exportTickets(
            @RequestBody ExportSearchRequestDTO request,
            HttpServletResponse response
    );



}
