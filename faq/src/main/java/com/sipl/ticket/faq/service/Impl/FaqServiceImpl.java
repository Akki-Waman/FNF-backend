package com.sipl.ticket.faq.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sipl.ticket.core.dao.entity.Faq;
import com.sipl.ticket.core.dao.entity.FaqCategory;
import com.sipl.ticket.core.dao.repository.FaqRepository;
import com.sipl.ticket.core.dao.repository.UsersRepository;
import com.sipl.ticket.core.dto.request.FaqSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import com.sipl.ticket.core.dto.response.FaqDto;
import com.sipl.ticket.core.mapper.FaqMapper;
import com.sipl.ticket.faq.service.FaqService;
import org.apache.commons.io.FilenameUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

    private final FaqRepository faqRepository;
    private final FaqMapper faqMapper;
    private final ObjectMapper objectMapper;
    private final UsersRepository userRepository;

    @Value("${faqFilePath}")
    private String faqBaseFilePath;

    public ApiResponseDTO<FaqDto> saveFaqDetails(
            String requestDto, MultipartFile attachment, MultipartFile image) {
        try {
            FaqDto faqDto = objectMapper.readValue(requestDto, FaqDto.class);
            validateFaqDto(faqDto);
            if (attachment != null && !attachment.isEmpty()) {
                String savedPath = saveFile(attachment);
                faqDto.setAttachmentPath(savedPath);
            }
            if (image != null && !image.isEmpty()) {
                String savedPath = saveFile(image);
                faqDto.setImagePath(savedPath);
            }
            Faq faq = faqMapper.toEntity(faqDto);
            faq.setIsActive(true);
            Faq savedFaq = faqRepository.save(faq);
            FaqDto responseDto = faqMapper.toDto(savedFaq);
            return new ApiResponseDTO<>(responseDto, "FAQ added successfully.", HttpStatus.OK, false);
        } catch (IllegalArgumentException ex) {
            log.warn("Validation failed for FAQ: {}", ex.getMessage());
            return new ApiResponseDTO<>(null, ex.getMessage(), HttpStatus.BAD_REQUEST, true);

        } catch (Exception e) {
            log.info("Error occured while saveFaqDetails", e);
            return new ApiResponseDTO<>(null,  "Error while adding FAQ. " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, true);

        }
    }


    public String saveFile(MultipartFile file) throws IOException {
        log.info("inside save file" + file);
        if (file != null && !file.isEmpty()) {
            String originalFileName = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(originalFileName);
            log.info("Extension :" + extension);
            File folder = new File(faqBaseFilePath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HHmmss");
            String timestamp = now.format(formatter);

            int fileNumber = 1;
            String fileName = "FAQ" + "_" + timestamp + "_" + fileNumber + "." + extension;
            File destFile = new File(folder, fileName);
            while (destFile.exists()) {
                fileNumber++;
                fileName = "FAQ" + "_" + timestamp + "_" + fileNumber + "." + extension;
                destFile = new File(folder, fileName);
            }
            file.transferTo(destFile);
            return "/" + fileName;
        }
        return null;
    }

    private void validateFaqDto(FaqDto faqDto) {
        if (faqDto == null) {
            throw new IllegalArgumentException("FAQ data is missing.");
        }

        if (faqDto.getQuestion() == null || faqDto.getQuestion().trim().isEmpty()) {
            throw new IllegalArgumentException("Question is required.");
        }

        if (faqDto.getAnswer() == null || faqDto.getAnswer().trim().isEmpty()) {
            throw new IllegalArgumentException("Answer is required.");
        }

        if (faqDto.getFaqCategory() == null || faqDto.getFaqCategory().getFaqCategoryId() == null) {
            throw new IllegalArgumentException("FAQ category is required.");
        }
    }



    @Override
    public ApiResponseDTO<PagedResponse<FaqDto>> searchFaq(FaqSearchRequestDto faqSearchRequestDto) {
        try {
            Integer pageNum = Optional.ofNullable(faqSearchRequestDto.getPage()).orElse(0);
            Integer pageSize = Optional.ofNullable(faqSearchRequestDto.getSize()).orElse(10);
            String sortBy = Optional.ofNullable(faqSearchRequestDto.getSortBy()).orElse("id");
            String sortDir = Optional.ofNullable(faqSearchRequestDto.getSortDir()).orElse("asc");
            Sort sort = sortDir.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
            Page<Faq> faqPage = faqRepository.findBySearchQuery(
                    faqSearchRequestDto.getQuestion(),
                    faqSearchRequestDto.getFaqCategoryId(),
                    pageable
            );
            if (faqPage.isEmpty()) {
                PagedResponse<FaqDto> emptyResponse = new PagedResponse<>(
                        Collections.emptyList(),
                        faqPage.getNumber(),
                        faqPage.getTotalElements(),
                        faqPage.getTotalPages(),
                        faqPage.getSize(),
                        faqPage.isLast()
                );
                return new ApiResponseDTO<>(
                        emptyResponse,
                        "No FAQs found for search.",
                        HttpStatus.OK,
                        false
                );
            }
            List<FaqDto> faqDtos = faqPage.getContent()
                    .stream()
                    .map(faqMapper::toDto)
                    .collect(Collectors.toList());
            PagedResponse<FaqDto> response = new PagedResponse<>(
                    faqDtos,
                    faqPage.getNumber(),
                    faqPage.getTotalElements(),
                    faqPage.getTotalPages(),
                    faqPage.getSize(),
                    faqPage.isLast()
            );
            return new ApiResponseDTO<>(
                    response,
                    "FAQs fetched successfully",
                    HttpStatus.OK,
                    false
            );
        } catch (IllegalArgumentException ex) {
            return new ApiResponseDTO<>(
                    null,
                    "Invalid request: " + ex.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    true
            );
        } catch (Exception ex) {
            log.error("An error occurred while processing searchFaq", ex);
            return new ApiResponseDTO<>(
                    null,
                    "An unexpected error occurred while searching FAQs: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    public ApiResponseDTO<FaqDto> getById(Integer faqId) {
        try {
            Optional<Faq> faqOpt = faqRepository.findById(faqId);

            if (faqOpt.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "Faq data not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            Faq faqData = faqOpt.get();
            FaqDto faqDto = faqMapper.toDto(faqData);

            return new ApiResponseDTO<>(
                    faqDto,
                    "Faq Data Found",
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
    public ApiResponseDTO<String> deleteById(Integer faqId) {
        try {
            Optional<Faq> faq = faqRepository.findById(faqId);
            if (!faq.isPresent()) {
                return new ApiResponseDTO<>(
                        null,
                        "Faq data not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            Faq faqData = faq.get();
            faqRepository.delete(faqData);

            return new ApiResponseDTO<>(
                    null,
                    "Faq Data deleted successfully",
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

    public ApiResponseDTO<FaqDto> getAll() {
        try {
            List<Faq> faq = faqRepository.findAll();
            if (faq.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "Faq data not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }
            List<FaqDto> faqDtolist = faqMapper.mapFaqListToDtoList(faq);
            return new ApiResponseDTO<>(
                    faqDtolist,  // this sets dataList
                    HttpStatus.OK,
                    "Faq Data Found",
                    false,
                    LocalDateTime.now()
            );

        } catch (Exception e) {
            log.error("An error occurred while processing getAll", e);

            return new ApiResponseDTO<>(
                    null,
                    "Server Error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    public ApiResponseDTO<FaqDto> updateFaqDetails(
            String requestDto, MultipartFile attachment, MultipartFile image) {
        try {
            FaqDto faqDto = objectMapper.readValue(requestDto, FaqDto.class);
            validateFaqDto(faqDto);

            Optional<Faq> optionalFaq = faqRepository.findById(faqDto.getFaqId());
            if (!optionalFaq.isPresent()) {
                return new ApiResponseDTO<>(
                        null,
                        "FAQ data not found",
                        HttpStatus.NOT_FOUND,
                        true
                );

            }

            Faq faqData = optionalFaq.get();
            if (attachment != null && !attachment.isEmpty()) {
                String savedPath = saveFile(attachment);
                faqDto.setAttachmentPath(savedPath);
            }
            if (image != null && !image.isEmpty()) {
                String savedPath = saveFile(image);
                faqDto.setImagePath(savedPath);
            }
            faqData.setQuestion(faqDto.getQuestion());
            faqData.setAnswer(faqDto.getAnswer());
            faqData.setIsActive(faqDto.getIsActive());
            if (faqDto.getFaqCategory() != null) {
                FaqCategory category = new FaqCategory();
                category.setFaqCategoryId(faqDto.getFaqCategory().getFaqCategoryId());
                faqData.setFaqCategory(category);
            }
            Faq savedFaq = faqRepository.save(faqData);
            FaqDto responseDto = faqMapper.toDto(savedFaq);
            return new ApiResponseDTO<>(
                    responseDto,
                    "FAQ updated successfully.",
                    HttpStatus.OK,
                    false
            );


        } catch (IllegalArgumentException ex) {
            log.warn("Validation failed for FAQ: {}", ex.getMessage());
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
                    "Error while updating FAQ: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    public ApiResponseDTO<FaqDto> raiseQuestion(FaqDto faqDto) {
        try {
            if (faqDto.getQuestion() == null || faqDto.getQuestion().trim().isEmpty()) {

                return new ApiResponseDTO<>(
                        null,
                        "Question cannot be empty.",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }
            Faq faq = new Faq();
            faq.setQuestion(faqDto.getQuestion().trim());
            faq.setIsActive(true);
            faqRepository.save(faq);

            return new ApiResponseDTO<>(
                    null,
                    "Question raised successfully.",
                    HttpStatus.OK,
                    false
            );
        } catch (Exception e) {

            return new ApiResponseDTO<>(
                    null,
                    "Error while raising question.",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }
}
