package com.sipl.ticket.faqCategory.service.Impl;

import com.sipl.ticket.core.dao.entity.FaqCategory;
import com.sipl.ticket.core.dao.repository.FaqCategoryRepository;
import com.sipl.ticket.core.dto.request.FaqCategorySearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.FaqCategoryDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.mapper.FaqCategoryMapper;
import com.sipl.ticket.faqCategory.service.FaqCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FaqCategoryServiceImpl implements FaqCategoryService {
    private final FaqCategoryRepository faqCategoryRepository;
    private final FaqCategoryMapper faqCategoryMapper;

    public ApiResponseDTO<FaqCategoryDto> saveFaqCategoryDetails(FaqCategoryDto faqCategoryDto) {
        try {
            validateFaqCategoryDto(faqCategoryDto);
            FaqCategory faqCategory = faqCategoryMapper.toEntity(faqCategoryDto);
            faqCategory.setIsActive(true);
            FaqCategory savedFaqCategory = faqCategoryRepository.save(faqCategory);
            FaqCategoryDto responseDto = faqCategoryMapper.toDto(savedFaqCategory);
            return new ApiResponseDTO<>(responseDto, "Faq category added successfully.", HttpStatus.OK, false);
        } catch (IllegalArgumentException ex) {
            log.warn("Validation failed for FaqCategory: {}", ex.getMessage());
            return new ApiResponseDTO<>(null, ex.getMessage(), HttpStatus.BAD_REQUEST, true);
        } catch (EntityNotFoundException ex) {
            log.info("Error occured while adding", ex);
            return new ApiResponseDTO<>(null, null, HttpStatus.NOT_FOUND, true);
        } catch (Exception e) {
            log.info("Error occured while saveFaqCategoryDetails", e);
            return new ApiResponseDTO<>(null, "Error while adding FaqCategory. " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, true);
        }
    }

    private void validateFaqCategoryDto(FaqCategoryDto faqCategoryDto) {
        if (faqCategoryDto == null) {
            throw new IllegalArgumentException("Faq category data is missing.");
        }

        if (faqCategoryDto.getCategoryDescription() == null
                || faqCategoryDto.getCategoryDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description is required.");
        }

        if (faqCategoryDto.getCategoryName() == null
                || faqCategoryDto.getCategoryName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name is required.");
        }
    }

    public ApiResponseDTO<PagedResponse<FaqCategoryDto>> searchFaqCategory(
            FaqCategorySearchRequestDto faqCategorySearchRequestDto) {
        try {
            int pageNum = faqCategorySearchRequestDto.getPage() > 0
                    ? faqCategorySearchRequestDto.getPage()
                    : 0;

            int pageSize = faqCategorySearchRequestDto.getSize() > 0
                    ? faqCategorySearchRequestDto.getSize()
                    : 10;


            String categoryName = faqCategorySearchRequestDto.getCategoryName();
            Integer categoryId = faqCategorySearchRequestDto.getCategoryId();

            Pageable pageable = PageRequest.of(pageNum, pageSize);

            Page<FaqCategory> faqCategoryPage =
                    faqCategoryRepository.findBySearchQuery(categoryName, categoryId, pageable);

            if (faqCategoryPage != null && !faqCategoryPage.isEmpty()) {
                List<FaqCategoryDto> faqCategoryDtos = new ArrayList<>();
                for (FaqCategory faqCategory : faqCategoryPage.getContent()) {
                    FaqCategoryDto dto = faqCategoryMapper.toDto(faqCategory);
                    faqCategoryDtos.add(dto);
                }

                PagedResponse<FaqCategoryDto> response = new PagedResponse<>(
                        faqCategoryDtos,
                        faqCategoryPage.getNumber(),
                        faqCategoryPage.getTotalElements(),
                        faqCategoryPage.getTotalPages(),
                        faqCategoryPage.getSize(),
                        faqCategoryPage.isLast()
                );

                return new ApiResponseDTO<>(
                        response,
                        "FAQ categories fetched successfully",
                        HttpStatus.OK,
                        false
                );
            } else {

                return new ApiResponseDTO<>(
                        null,
                        "Faq category data not found.",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

        } catch (Exception e) {
            log.error("An error occurred while processing searchFaqCategory", e);
            return new ApiResponseDTO<>(
                    null,
                    "Server Error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    public ApiResponseDTO<FaqCategoryDto> getById(Integer faqCategoryId) {
        try {
            Optional<FaqCategory> faqCategory = faqCategoryRepository.findById(faqCategoryId);
            if (!faqCategory.isPresent()) {
                return new ApiResponseDTO<>(
                        null,
                        "Faq category data not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }
            FaqCategory faqCategoryData = faqCategory.get();
            FaqCategoryDto faqCategoryDto = faqCategoryMapper.toDto(faqCategoryData);

            return new ApiResponseDTO<>(
                    faqCategoryDto,
                    "FaqCategory Data Found",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("An error occurred while processing getById", e);

            return new ApiResponseDTO<>(
                    null,
                    "Server Error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    public ApiResponseDTO<String> deleteById(Integer faqCategoryId) {
        try {
            Optional<FaqCategory> faqCategory = faqCategoryRepository.findById(faqCategoryId);
            if (!faqCategory.isPresent()) {
                return new ApiResponseDTO<>(
                        null,
                        "Faq category data not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }
            FaqCategory faqData = faqCategory.get();
            faqCategoryRepository.delete(faqData);
            return new ApiResponseDTO<>(
                    null,
                    "Faq category data deleted successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("An error occurred while processing deleteById", e);

            return new ApiResponseDTO<>(
                    null,
                    "Server Error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    public ApiResponseDTO<FaqCategoryDto> getAll() {
        try {
            List<FaqCategory> faqCategory = faqCategoryRepository.findAll();
            if (faqCategory.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "FaqCategory data not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }
            List<FaqCategoryDto> faqCategoryDto =
                    faqCategoryMapper.mapFaqCategoryListToDtoList(faqCategory);
            return new ApiResponseDTO<>(
                    faqCategoryDto,
                    HttpStatus.OK,
                    "FaqCategory Data Found",
                    false,
                    LocalDateTime.now()
            );

        } catch (Exception e) {
            log.error("An error occurred while processing getAll", e);
            return new ApiResponseDTO<>(
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Server Error",
                    true,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponseDTO<FaqCategoryDto> updateFaqCategoryDetails(FaqCategoryDto faqCategoryDto) {
        try {
            validateFaqCategoryDto(faqCategoryDto);

            Optional<FaqCategory> optionalFaqCategory =
                    faqCategoryRepository.findById(faqCategoryDto.getFaqCategoryId());
            if (!optionalFaqCategory.isPresent()) {
                return new ApiResponseDTO<>(
                        null,
                        "FaqCategory data not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }
            FaqCategory faqCategoryData = optionalFaqCategory.get();
            faqCategoryData.setCategoryDescription(faqCategoryDto.getCategoryDescription());
            faqCategoryData.setCategoryName(faqCategoryDto.getCategoryName());
            FaqCategory savedFaqCategory = faqCategoryRepository.save(faqCategoryData);
            FaqCategoryDto responseDto = faqCategoryMapper.toDto(savedFaqCategory);
            return new ApiResponseDTO<>(
                    responseDto,
                    "Faq category updated successfully.",
                    HttpStatus.OK,
                    false
            );

        } catch (IllegalArgumentException ex) {
            log.warn("Validation failed for faq category: {}", ex.getMessage());
            return new ApiResponseDTO<>(
                    null,
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    true
            );

        } catch (EntityNotFoundException ex) {
            log.error("Entity not found during update", ex);
            return new ApiResponseDTO<>(
                    null,
                    "Referenced entity not found",
                    HttpStatus.NOT_FOUND,
                    true
            );

        } catch (Exception e) {
            log.error("Unexpected error during update", e);
            return new ApiResponseDTO<>(
                    null,
                    "Error while updating faq category: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }

    }
}