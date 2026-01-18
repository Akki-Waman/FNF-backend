package com.sipl.ticket.service.impl;

import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.core.dao.entity.Tags;
import com.sipl.ticket.core.dao.repository.TagsRepository;
import com.sipl.ticket.core.dto.request.TagsRequestDto;
import com.sipl.ticket.core.dto.request.TagsSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.TagResponseDto;
import com.sipl.ticket.core.helper.TagExcelGenerator;
import com.sipl.ticket.core.mapper.TagsMapper;
import com.sipl.ticket.core.util.PaginationUtil;
import com.sipl.ticket.service.TagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TagsServiceImpl implements TagsService {

    private final TagsRepository repository;
    private final TagsMapper mapper;

    @Override
    @CacheEvict(value = "tags", allEntries = true)
    @ActivityLoggable(
            action = "CREATE",
            module = "TAG",
            description = "Tag {0} created successfully"
    )
    public ApiResponseDTO<TagResponseDto> saveTag(TagsRequestDto dto) {

        try {
            String name = dto.getTagName().trim();

            if (repository.existsByTagNameIgnoreCase(name)) {
                return new ApiResponseDTO<>(
                        null,
                        "Tag '" + name + "' already exists",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            Tags tag = new Tags();
            tag.setTagName(name);
            tag.setIsActive(true);
            tag.setIsDelete(false);
            Tags saved = repository.save(tag);

            return new ApiResponseDTO<>(
                    mapper.toDto(saved),
                    "Tag created successfully",
                    HttpStatus.CREATED,
                    false
            );

        } catch (Exception e) {
            log.error("saveTag error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @CacheEvict(value = "tags", allEntries = true)
    @ActivityLoggable(
            action = "UPDATE",
            module = "TAG",
            description = "Tag {0} updated successfully"
    )
    public ApiResponseDTO<TagResponseDto> updateTag(TagsRequestDto dto) {

        try {
            if (dto.getTagId() == null ||
                    dto.getTagName() == null ||
                    dto.getTagName().trim().isEmpty()) {

                return new ApiResponseDTO<>(
                        null,
                        "Tag ID and name are required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            Tags tag = repository.findById(dto.getTagId()).orElse(null);

            if (tag == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Tag not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }
            if (Boolean.TRUE.equals(tag.getIsDelete())) {
                return new ApiResponseDTO<>(
                        null,
                        "Cannot update deleted tag",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }


            if (Boolean.FALSE.equals(tag.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Inactive tag cannot be updated",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            String name = dto.getTagName().trim();

            if (repository.existsByTagNameIgnoreCaseAndTagIdNot(
                    name, dto.getTagId())) {

                return new ApiResponseDTO<>(
                        null,
                        "Tag '" + name + "' already exists",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            tag.setTagName(name);
            Tags updated = repository.save(tag);

            return new ApiResponseDTO<>(
                    mapper.toDto(updated),
                    "Tag updated successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("updateTag error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<TagResponseDto> getById(Long id) {

        try {
            return repository.findById(id)
                    .filter(t ->
                            Boolean.TRUE.equals(t.getIsActive()) &&
                                    Boolean.FALSE.equals(t.getIsDelete())
                    )
                    .map(t -> new ApiResponseDTO<>(
                            mapper.toDto(t),
                            "Tag found",
                            HttpStatus.OK,
                            false
                    ))
                    .orElseGet(() -> new ApiResponseDTO<>(
                            null,
                            "Tag not found",
                            HttpStatus.NOT_FOUND,
                            true
                    ));

        } catch (Exception e) {
            log.error("getTagById error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @CacheEvict(value = "tags", allEntries = true)
    @ActivityLoggable(
            action = "DELETE",
            module = "TAG",
            description = "Tag id {0} deleted successfully"
    )
    public ApiResponseDTO<String> deleteById(Long id) {

        try {
            Tags tag = repository.findById(id).orElse(null);

            if (tag == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Tag not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.FALSE.equals(tag.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Tag already inactive",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            tag.setIsActive(false);
            tag.setIsDelete(true);
            repository.save(tag);

            return new ApiResponseDTO<>(
                    null,
                    "Tag deleted successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("deleteTag error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @Cacheable("tags")
    public ApiResponseDTO<TagResponseDto> getAllTags() {

        try {
            List<TagResponseDto> list = repository
                    .findAll(Sort.by(Sort.Direction.DESC, "tagId"))
                    .stream()
                    .filter(t ->
                            Boolean.TRUE.equals(t.getIsActive()) &&
                                    Boolean.FALSE.equals(t.getIsDelete())
                    )
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            if (list.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No tags found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            return new ApiResponseDTO<>(
                    list,
                    HttpStatus.OK,
                    "Tags fetched successfully",
                    false,
                    LocalDateTime.now()
            );

        } catch (Exception e) {
            log.error("getAllTags error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<PagedResponse<TagResponseDto>> searchTags(
            TagsSearchRequestDto dto) {

        try {
            Pageable pageable = PaginationUtil.pageable(
                    dto.getPage(),
                    dto.getSize(),
                    dto.getSortBy(),
                    dto.getSortDir()
            );

            Page<Tags> pageResult =
                    repository.searchTags(
                            dto.getQuery(),
                            dto.getIsActive(),
                            pageable
                    );

            if (pageResult.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No tags found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            List<TagResponseDto> content = pageResult.getContent()
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            PagedResponse<TagResponseDto> pagedResponse =
                    new PagedResponse<>(
                            content,
                            pageResult.getNumber(),
                            pageResult.getTotalElements(),
                            pageResult.getTotalPages(),
                            pageResult.getSize(),
                            pageResult.isLast()
                    );

            return new ApiResponseDTO<>(
                    pagedResponse,
                    "Tags fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("searchTags error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public void exportTagsCsv(HttpServletResponse response) {

        log.info("Exporting active tags to CSV");

        try {
            List<TagResponseDto> tags = repository.findAll()
                    .stream()
                    .filter(t ->
                            Boolean.TRUE.equals(t.getIsActive()) &&
                                    Boolean.FALSE.equals(t.getIsDelete())
                    )
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            TagExcelGenerator.generateExcel(tags, response);

            log.info("Tags CSV export completed successfully, totalRecords={}",
                    tags.size());

        } catch (Exception e) {
            log.error("exportTagsCsv unexpected error", e);
            throw new RuntimeException("Failed to export tags CSV", e);
        }
    }

}
