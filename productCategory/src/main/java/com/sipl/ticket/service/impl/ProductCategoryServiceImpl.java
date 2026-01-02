package com.sipl.ticket.service.impl;

import com.sipl.ticket.core.dao.entity.ProductCategories;
import com.sipl.ticket.core.dao.repository.ProductCategoryRepository;
import com.sipl.ticket.core.dto.request.ProductCategoryRequestDto;
import com.sipl.ticket.core.dto.request.ProductCategorySearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ProductCategoryDto;
import com.sipl.ticket.core.helper.ProductCategoryExcelGenerator;
import com.sipl.ticket.core.mapper.ProductCategoryMapper;
import com.sipl.ticket.service.ProductCategoryService;
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
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository repository;
    private final ProductCategoryMapper mapper;

    @Override
    @CacheEvict(value = "productCategories", allEntries = true)
    public ApiResponseDTO<ProductCategoryDto> saveProductCategory(
            ProductCategoryRequestDto dto) {

        log.info("Saving product category with name: {}", dto.getProductCategoryName());

        try {
            String name = dto.getProductCategoryName().trim();

            if (repository.existsByProductCategoryNameIgnoreCase(name)) {
                return new ApiResponseDTO<>(
                        null,
                        "Product category '" + name + "' already exists.",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            ProductCategories category = new ProductCategories();
            category.setProductCategoryName(name);
            category.setIsActive(true);

            ProductCategories savedCategory = repository.save(category);

            return new ApiResponseDTO<>(
                    mapper.toDto(savedCategory),
                    "Product category created successfully",
                    HttpStatus.CREATED,
                    false
            );

        } catch (Exception e) {
            log.error("Error occurred while saving product category", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @CacheEvict(value = "productCategories", allEntries = true)
    public ApiResponseDTO<ProductCategoryDto> updateProductCategory(
            ProductCategoryRequestDto dto) {

        log.info(
                "Updating product category, id={}, name={}",
                dto.getProductCategoryId(),
                dto.getProductCategoryName()
        );

        try {
            if (dto == null || dto.getProductCategoryId() == null ||
                    dto.getProductCategoryName() == null ||
                    dto.getProductCategoryName().trim().isEmpty()) {

                return new ApiResponseDTO<>(
                        null,
                        "Product category ID and name are required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            ProductCategories category =
                    repository.findById(dto.getProductCategoryId()).orElse(null);

            if (category == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Product category not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.FALSE.equals(category.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Inactive product category cannot be updated",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            String name = dto.getProductCategoryName().trim();

            if (repository.existsByProductCategoryNameIgnoreCaseAndProductCategoryIdNot(
                    name, dto.getProductCategoryId())) {

                return new ApiResponseDTO<>(
                        null,
                        "Product category with the name '" + name + "' already exists.",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            category.setProductCategoryName(name);
            ProductCategories updatedCategory = repository.save(category);

            return new ApiResponseDTO<>(
                    mapper.toDto(updatedCategory),
                    "Product category updated successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("updateProductCategory unexpected error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<ProductCategoryDto> getById(Long id) {

        log.info("Fetching product category by id={}", id);

        try {
            return repository.findById(id)
                    .filter(c -> Boolean.TRUE.equals(c.getIsActive()))
                    .map(c -> new ApiResponseDTO<>(
                            mapper.toDto(c),
                            "Product category found",
                            HttpStatus.OK,
                            false
                    ))
                    .orElseGet(() -> new ApiResponseDTO<>(
                            null,
                            "Product category not found",
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
    @CacheEvict(value = "productCategories", allEntries = true)
    public ApiResponseDTO<String> deleteById(Long id) {

        log.info("Deactivating product category, id={}", id);

        try {
            ProductCategories category = repository.findById(id).orElse(null);

            if (category == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Product category not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.FALSE.equals(category.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Product category is already inactive",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            category.setIsActive(false);
            repository.save(category);

            return new ApiResponseDTO<>(
                    null,
                    "Product category deleted successfully",
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
    @Cacheable("productCategories")
    public ApiResponseDTO<PagedResponse<ProductCategoryDto>> getAllProductCategories() {

        log.info("Fetching all active product categories");

        try {
            List<ProductCategoryDto> response = repository.findAll()
                    .stream()
                    .filter(c -> Boolean.TRUE.equals(c.getIsActive()))
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            if (response.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No product categories found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            return new ApiResponseDTO<>(
                    new PagedResponse<>(
                            response,
                            0,
                            response.size(),
                            1,
                            response.size(),
                            true
                    ),
                    "Product categories fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("getAllProductCategories unexpected error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }
    @Override
    public void exportProductCategoriesExcel(HttpServletResponse response) {

        log.info("Exporting active product categories to Excel");

        try {
            List<ProductCategoryDto> categories = repository.findAll()
                    .stream()
                    .filter(c -> Boolean.TRUE.equals(c.getIsActive()))
                    .map(c -> new ProductCategoryDto(
                            c.getProductCategoryId(),
                            c.getProductCategoryName(),
                            c.getIsActive()
                    ))
                    .collect(Collectors.toList());

            ProductCategoryExcelGenerator.generateExcel(categories, response);

            log.info(
                    "Product Category Excel export completed successfully, totalRecords={}",
                    categories.size()
            );

        } catch (Exception e) {
            log.error("exportProductCategoriesExcel unexpected error", e);
            throw new RuntimeException(
                    "Failed to export product categories Excel", e
            );
        }
    }

    @Override
    public ApiResponseDTO<PagedResponse<ProductCategoryDto>> searchProductCategories(
            ProductCategorySearchRequestDto dto) {

        try {
            Sort sort = "asc".equalsIgnoreCase(dto.getSortDir())
                    ? Sort.by(dto.getSortBy()).ascending()
                    : Sort.by(dto.getSortBy()).descending();

            Pageable pageable = PageRequest.of(
                    dto.getPage(),
                    dto.getSize(),
                    sort
            );

            Page<ProductCategories> pageResult =
                    repository.searchByProductCategoryId(
                            dto.getProductCategoryId(),
                            pageable
                    );

            if (pageResult.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No product categories found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            return new ApiResponseDTO<>(
                    new PagedResponse<>(
                            mapper.toDtoList(pageResult.getContent()),
                            pageResult.getNumber(),
                            pageResult.getTotalElements(),
                            pageResult.getTotalPages(),
                            pageResult.getSize(),
                            pageResult.isLast()
                    ),
                    "Product categories fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("searchProductCategories error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }
}
