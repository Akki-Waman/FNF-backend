package com.sipl.ticket.service.impl;

import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.core.dao.entity.Brands;
import com.sipl.ticket.core.dao.entity.Companies;
import com.sipl.ticket.core.dao.repository.BrandRepository;
import com.sipl.ticket.core.dao.repository.CompanyRepository;
import com.sipl.ticket.core.dto.request.BrandSearchRequestDto;
import com.sipl.ticket.core.dto.request.BrandsRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.BrandDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.helper.BrandExcelGenerator;
import com.sipl.ticket.core.mapper.BrandsMapper;
import com.sipl.ticket.core.util.PaginationUtil;
import com.sipl.ticket.service.BrandsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
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
public class BrandsServiceImpl implements BrandsService {

    private final BrandRepository repository;
    private final BrandsMapper mapper;
    private final CompanyRepository companyRepository;

    @ActivityLoggable(
            action = "CREATE",
            module = "BRAND",
            description = "Brand {0} created successfully"
    )
    public ApiResponseDTO<BrandDto> saveBrand(BrandsRequestDto dto) {

        log.info("Saving brand with name: {}", dto.getBrandName());

        try {
            String name = dto.getBrandName().trim();

            Companies company = companyRepository.findById(dto.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Company not found"));

            if (repository.existsActiveBrandForCompany(name, dto.getCompanyId())) {
                return new ApiResponseDTO<>(
                        null,
                        "Brand '" + name + "' already exists.",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            Brands brand = new Brands();
            brand.setBrandName(name);
            brand.setCompany(company);
            brand.setIsActive(true);
            brand.setIsDeleted(false);

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





    @ActivityLoggable(
            action = "UPDATE",
            module = "BRAND",
            description = "Brand {0} updated successfully"
    )
    public ApiResponseDTO<BrandDto> updateBrand(BrandsRequestDto dto) {

        log.info("Updating brand, id={}, name={}, isActive={}",
                dto != null ? dto.getBrandId() : null,
                dto != null ? dto.getBrandName() : null,
                dto != null ? dto.getIsActive() : null);

        try {
            if (dto == null || dto.getBrandId() == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Brand ID is required",
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

            if (Boolean.TRUE.equals(brand.getIsDeleted())) {
                return new ApiResponseDTO<>(
                        null,
                        "Brand is deleted",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            boolean isUpdated = false;

            if (dto.getBrandName() != null &&
                    !dto.getBrandName().trim().isEmpty()) {

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
                isUpdated = true;
            }

            if (dto.getIsActive() != null) {
                brand.setIsActive(dto.getIsActive());
                isUpdated = true;
            }

            if (!isUpdated) {
                return new ApiResponseDTO<>(
                        null,
                        "No fields provided to update",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

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
                    .filter(b -> Boolean.FALSE.equals(b.getIsDeleted()))
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

    @ActivityLoggable(
            action = "DELETE",
            module = "BRAND",
            description = "Brand {0} deleted successfully"
    )
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

            if (Boolean.TRUE.equals(brand.getIsDeleted())) {
                return new ApiResponseDTO<>(
                        null,
                        "Brand already deleted",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }
            brand.setIsDeleted(true);
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
    public ApiResponseDTO<BrandDto> getAllBrands() {

        log.info("Fetching all active brands");

        try {
            List<BrandDto> list = repository.findAll(Sort.by(Sort.Direction.ASC, "brandName"))
                    .stream()
                    .filter(b -> Boolean.TRUE.equals(b.getIsActive()))
                    .filter(b -> Boolean.FALSE.equals(b.getIsDeleted()))
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            if (list.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No brands found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            return new ApiResponseDTO<>(
                    list,
                    HttpStatus.OK,
                    "Brands fetched successfully",
                    false,
                    LocalDateTime.now()
            );

        } catch (Exception e) {
            log.error("getAllBrands error", e);
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
            Pageable pageable = PaginationUtil.pageable(
                    dto.getPage(),
                    dto.getSize(),
                    dto.getSortBy(),
                    dto.getSortDir()
            );

            Page<Brands> pageResult =
                    repository.searchBrands(
                            dto.getQuery(),
                            dto.getIsActive(),
                            dto.getCompanyId(),
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
                    .filter(b -> Boolean.FALSE.equals(b.getIsDeleted()))
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
