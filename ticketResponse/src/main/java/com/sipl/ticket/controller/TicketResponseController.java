package com.sipl.ticket.controller;

import com.sipl.ticket.core.dto.request.ExportSearchRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CombinedTaskResponseDto;
import com.sipl.ticket.core.dto.response.CombinedTicketResponseDto;
import com.sipl.ticket.core.dto.response.TicketResponseCombinedDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping("/api/v1/ticket-response")
@RestController
@CrossOrigin(origins = "*")
@Api(tags = "Tickets Response APIs")
public interface TicketResponseController {

    @ApiOperation(
            value = "Create a new ticket response",
            notes = "Provide ticket response details to create a new ticket response"
    )
    @PostMapping("/save")
    public ResponseEntity<ApiResponseDTO<TicketResponseCombinedDto>> addTicketResponse(
            @RequestPart("ticketResponseRequestDto") String ticketResponseRequestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    );


}
