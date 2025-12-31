package com.sipl.ticket.service.impl;

import com.sipl.ticket.core.dao.entity.Brands;
import com.sipl.ticket.core.dao.repository.BrandRepository;
import com.sipl.ticket.core.dto.request.BrandSearchRequestDto;
import com.sipl.ticket.core.dto.request.BrandsRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.BrandDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.helper.BrandExcelGenerator;
import com.sipl.ticket.core.mapper.BrandsMapper;
import com.sipl.ticket.service.BrandsService;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BrandsServiceImpl implements BrandsService {

    private final BrandRepository repository;
    private final BrandsMapper mapper;

    @Override
    @CacheEvict(value = "brands", allEntries = true)
    public ApiResponseDTO<BrandDto> saveBrand(BrandsRequestDto dto) {

        log.info("Saving brand with name: {}", dto.getBrandName());

        try {
            String name = dto.getBrandName().trim();

            if (repository.existsByBrandNameIgnoreCase(name)) {
                return new ApiResponseDTO<>(
                        null,
                        "Brand '" + name + "' already exists.",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            Brands brand = new Brands();
            brand.setBrandName(name);
            brand.setIsActive(true);

            Brands savedBrand = repository.save(brand);

            return new ApiResponseDTO<>(
                    mapper.toDto(savedBrand),
                    "Brand created successfully",
                    HttpStatus.CREATED,
                    false
            );

        } catch (Exception e) {
            log.error("Error occurred while saving brand", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @CacheEvict(value = "brands", allEntries = true)
    public ApiResponseDTO<BrandDto> updateBrand(BrandsRequestDto dto) {

        log.info("Updating brand, id={}, name={}", dto.getBrandId(), dto.getBrandName());

        try {
            if (dto == null || dto.getBrandId() == null ||
                    dto.getBrandName() == null || dto.getBrandName().trim().isEmpty()) {

                return new ApiResponseDTO<>(
                        null,
                        "Brand ID and name are required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            Brands brand = repository.findById(dto.getBrandId()).orElse(null);

            if (brand == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Brand not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.FALSE.equals(brand.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Inactive brand cannot be updated",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            String name = dto.getBrandName().trim();

            if (repository.existsByBrandNameIgnoreCaseAndBrandIdNot(
                    name, dto.getBrandId())) {

                return new ApiResponseDTO<>(
                        null,
                        "Brand with the name '" + name + "' already exists.",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            brand.setBrandName(name);
            Brands updatedBrand = repository.save(brand);

            return new ApiResponseDTO<>(
                    mapper.toDto(updatedBrand),
                    "Brand updated successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("updateBrand unexpected error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<BrandDto> getById(Long id) {

        log.info("Fetching brand by id={}", id);

        try {
            return repository.findById(id)
                    .filter(b -> Boolean.TRUE.equals(b.getIsActive()))
                    .map(b -> new ApiResponseDTO<>(
                            mapper.toDto(b),
                            "Brand found",
                            HttpStatus.OK,
                            false
                    ))
                    .orElseGet(() -> new ApiResponseDTO<>(
                            null,
                            "Brand not found",
                            HttpStatus.NOT_FOUND,
                            true
                    ));

        } catch (Exception e) {
            log.error("getById unexpected error, id={}", id, e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @CacheEvict(value = "brands", allEntries = true)
    public ApiResponseDTO<String> deleteById(Long id) {

        log.info("Deactivating brand, id={}", id);

        try {
            Brands brand = repository.findById(id).orElse(null);

            if (brand == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Brand not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.FALSE.equals(brand.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Brand is already inactive",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            brand.setIsActive(false);
            repository.save(brand);

            return new ApiResponseDTO<>(
                    null,
                    "Brand deleted successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("deleteById unexpected error, id={}", id, e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @Cacheable("brands")
    public ApiResponseDTO<PagedResponse<BrandDto>> getAllBrands() {

        log.info("Fetching all active brands");

        try {
            List<BrandDto> response = repository.findAll()
                    .stream()
                    .filter(b -> Boolean.TRUE.equals(b.getIsActive()))
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            if (response.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No brands found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            return new ApiResponseDTO<>(
                    new PagedResponse<>(response, 0, response.size(), 1, response.size(), true),
                    "Brands fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("getAllBrands unexpected error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<PagedResponse<BrandDto>> searchBrands(
            BrandSearchRequestDto dto) {

        try {
            Sort sort = dto.getSortDir().equalsIgnoreCase("asc")
                    ? Sort.by(dto.getSortBy()).ascending()
                    : Sort.by(dto.getSortBy()).descending();

            Pageable pageable = PageRequest.of(
                    dto.getPage(),
                    dto.getSize(),
                    sort
            );

            Page<Brands> pageResult =
                    repository.searchByBrandId(
                            dto.getBrandId(),
                            pageable
                    );

            if (pageResult.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No brands found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            List<BrandDto> content = pageResult.getContent()
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            PagedResponse<BrandDto> pagedResponse =
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
                    "Brands fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("searchBrands error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }
    @Override
    @Transactional(readOnly = true)
    public void exportBrandsCsv(HttpServletResponse response) {

        log.info("Exporting active brands to CSV");

        try {
            List<BrandDto> brands = repository.findAll()
                    .stream()
                    .filter(b -> Boolean.TRUE.equals(b.getIsActive()))
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            BrandExcelGenerator.generateExcel(brands, response);

            log.info("Brands CSV export completed successfully, totalRecords={}",
                    brands.size());

        } catch (Exception e) {
            log.error("exportBrandsCsv unexpected error", e);
            throw new RuntimeException("Failed to export brands CSV", e);
        }
    }



}
