package com.sipl.ticket.controller;


import com.sipl.ticket.core.dto.request.*;
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
    @PostMapping("/delete")
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
    @PutMapping("/update")
    public ResponseEntity<ApiResponseDTO<CombinedTicketResponseDto>> updateTickets(
            @RequestPart(value = "ticketRequestDto") String ticketRequestDto,
            @RequestPart(value = "image", required = false) List<MultipartFile> multipartFile);


    @GetMapping("/ticket-summary")
    ResponseEntity<ApiResponseDTO<SummaryKpiResponseDTO>> getTikctSummary();

    @ApiOperation("Export tickets in Excel / CSV / PDF")
    @PostMapping("/export")
    ResponseEntity<Void> exportTickets(
            @RequestBody ExportSearchRequestDTO request,
            HttpServletResponse response
    );

    @ApiOperation(
            value = "Get list of all ticket IDs",
            notes = "Fetches list of active ticket IDs"
    )
    @GetMapping("/ids")
    ResponseEntity<ApiResponseDTO<Long>> getAllTicketIds();

    @GetMapping("/get/{ticketId}")
    ResponseEntity<ApiResponseDTO<CombinedTicketNoteResponseDto>> getByTicketId(
            @PathVariable Long ticketId
    );

    @ApiOperation(
            value = "Update a existing ticket status",
            notes = "Provide the necessary ticket information to save a new entry")
    @PutMapping("/status")
    public ResponseEntity<ApiResponseDTO<TicketCombinedResponseDto>> updateTicketStatus(
            @RequestBody TicketStatusRequestDTO ticketStatusRequestDTO);

    @PutMapping("/request-approval")
    public ResponseEntity<ApiResponseDTO<String>> requestTicketApproval(
            @RequestBody ApprovalRequestDTO dto);

    @ApiOperation(
            value = "Get list of all ticket IDs",
            notes = "Fetches list of active ticket IDs"
    )
    @GetMapping("/getByids")
    ResponseEntity<ApiResponseDTO<TicketCustomResponseDto>> getAllTicketCustomDetails();

}
