package com.sipl.ticket.controller;

import com.sipl.ticket.core.dto.request.TagsRequestDto;
import com.sipl.ticket.core.dto.request.TagsSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.TagResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequestMapping("/api/v1/tags")
@RestController
@CrossOrigin(origins = "*")
@Api(tags = "Tag APIs")
public interface TagsController {

    @ApiOperation(
            value = "Create a new tag",
            notes = "Provide the necessary tag information to save a new tag",
            response = TagResponseDto.class
    )
    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<TagResponseDto>> saveTag(
            @RequestBody TagsRequestDto tagsRequestDto
    );

    @ApiOperation(
            value = "Update tag details",
            notes = "Provide tag ID and updated tag information",
            response = TagResponseDto.class
    )
    @PutMapping("/update")
    ResponseEntity<ApiResponseDTO<TagResponseDto>> updateTag(
            @RequestBody TagsRequestDto tagsRequestDto
    );

    @ApiOperation(
            value = "Get tag by ID",
            notes = "Fetch tag details using tag ID",
            response = TagResponseDto.class
    )
    @GetMapping("/get/{tagId}")
    ResponseEntity<ApiResponseDTO<TagResponseDto>> getById(
            @PathVariable Long tagId
    );

    @ApiOperation(
            value = "Delete tag",
            notes = "Soft delete tag by tag ID",
            response = String.class
    )
    @DeleteMapping("/delete/{tagId}")
    ResponseEntity<ApiResponseDTO<String>> deleteById(
            @PathVariable Long tagId
    );

    @ApiOperation(
            value = "Get all tags",
            notes = "Fetch all active tags in descending order of tagId",
            response = TagResponseDto.class
    )
    @GetMapping("/getAll")
    ResponseEntity<ApiResponseDTO<TagResponseDto>> getAllTags();

    @ApiOperation(
            value = "Search tags",
            notes = "Search tags with pagination, sorting and filters",
            response = TagResponseDto.class
    )
    @PostMapping("/search")
    ResponseEntity<ApiResponseDTO<PagedResponse<TagResponseDto>>> searchTags(
            @RequestBody TagsSearchRequestDto requestDto
    );
    @GetMapping("/downloadTagsExcel")
    ResponseEntity<Void> exportTagsCsv(HttpServletResponse response);

}
