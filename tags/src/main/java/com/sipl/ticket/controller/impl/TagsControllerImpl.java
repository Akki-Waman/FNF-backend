package com.sipl.ticket.controller.impl;

import com.sipl.ticket.controller.TagsController;
import com.sipl.ticket.core.dto.request.TagsRequestDto;
import com.sipl.ticket.core.dto.request.TagsSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.TagResponseDto;
import com.sipl.ticket.service.TagsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TagsControllerImpl implements TagsController {

    private final TagsService tagsService;

    @Override
    public ResponseEntity<ApiResponseDTO<TagResponseDto>> saveTag(
            @Valid @RequestBody TagsRequestDto tagsRequestDto) {

        log.info("<<Start>> saveTag endpoint called <<Start>>");

        ApiResponseDTO<TagResponseDto> response =
                tagsService.saveTag(tagsRequestDto);

        log.info("<<End>> saveTag endpoint called <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<TagResponseDto>> updateTag(
            @Valid @RequestBody TagsRequestDto tagsRequestDto) {

        log.info("<<Start>> updateTag endpoint called <<Start>>");

        ApiResponseDTO<TagResponseDto> response =
                tagsService.updateTag(tagsRequestDto);

        log.info("<<End>> updateTag endpoint called <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<TagResponseDto>> getById(
            Long tagId) {

        log.info("<<Start>> getById endpoint called <<Start>>");

        ApiResponseDTO<TagResponseDto> response =
                tagsService.getById(tagId);

        log.info("<<End>> getById endpoint called <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteById(
            Long tagId) {

        log.info("<<Start>> deleteById endpoint called <<Start>>");

        ApiResponseDTO<String> response =
                tagsService.deleteById(tagId);

        log.info("<<End>> deleteById endpoint called <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<TagResponseDto>> getAllTags() {

        log.info("<<Start>> getAllTags endpoint called <<Start>>");

        ApiResponseDTO<TagResponseDto> response =
                tagsService.getAllTags();

        log.info("<<End>> getAllTags endpoint called <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<TagResponseDto>>> searchTags(
            @Valid @RequestBody TagsSearchRequestDto requestDto) {

        log.info("<<Start>> searchTags endpoint called <<Start>>");

        ApiResponseDTO<PagedResponse<TagResponseDto>> response =
                tagsService.searchTags(requestDto);

        log.info("<<End>> searchTags endpoint called <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<Void> exportTagsCsv(HttpServletResponse response) {

        log.info("<<Start>> exportTagsCsv endpoint called <<Start>>");

        tagsService.exportTagsCsv(response);

        log.info("<<End>> exportTagsCsv endpoint called <<End>>");

        return ResponseEntity.ok().build();
    }

}
