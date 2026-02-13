package com.sipl.ticket.controller;

import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.TicketResolutionCombinedDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/v1/ticket-resolution")
@RestController
@CrossOrigin(origins = "*")
@Api(tags = "Tickets Resolution APIs")
public interface TicketResolutionController {

    @ApiOperation(
            value = "Create ticket resolution",
            notes = "Provide ticket resolution details to resolve a ticket"
    )
    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<TicketResolutionCombinedDto>> addTicketResolution(
            @RequestPart("ticketResolutionRequestDto") String ticketResolutionRequestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    );
}
