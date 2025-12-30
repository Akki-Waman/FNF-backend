package com.sipl.ticket.service;

import com.sipl.ticket.core.dto.request.TagsRequestDto;
import com.sipl.ticket.core.dto.request.TagsSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.TagResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface TagsService {

    ApiResponseDTO<TagResponseDto> saveTag(
            TagsRequestDto tagsRequestDto
    );

    ApiResponseDTO<TagResponseDto> updateTag(
            TagsRequestDto tagsRequestDto
    );

    ApiResponseDTO<TagResponseDto> getById(
            Long tagId
    );

    ApiResponseDTO<String> deleteById(
            Long tagId
    );

    ApiResponseDTO<TagResponseDto> getAllTags();

    ApiResponseDTO<PagedResponse<TagResponseDto>> searchTags(
            TagsSearchRequestDto requestDto
    );
}
