package com.sipl.ticket.controller;

import com.sipl.ticket.core.dto.request.ContactRequestDto;
import com.sipl.ticket.core.dto.request.ContactSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ContactResponseDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/contact")
@CrossOrigin("*")
@Api(tags = "Contact APIs")
public interface ContactController {

    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<ContactResponseDto>> saveContact(
            @Valid @RequestBody ContactRequestDto contactRequestDto
    );

    @PutMapping("/update")
    ResponseEntity<ApiResponseDTO<ContactResponseDto>> updateContact(
            @Valid @RequestBody ContactRequestDto contactRequestDto
    );

    @GetMapping("/get/{contactId}")
    ResponseEntity<ApiResponseDTO<ContactResponseDto>> getById(
            @PathVariable Long contactId
    );

    @DeleteMapping("/delete/{contactId}")
    ResponseEntity<ApiResponseDTO<String>> deleteById(
            @PathVariable Long contactId
    );

    @GetMapping("/getAll")
    ResponseEntity<ApiResponseDTO<PagedResponse<ContactResponseDto>>> getAllContacts();

    @PostMapping("/search")
    ResponseEntity<ApiResponseDTO<PagedResponse<ContactResponseDto>>> searchContacts(
            @RequestBody ContactSearchRequestDto searchRequestDto
    );


}
