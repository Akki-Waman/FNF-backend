package com.sipl.ticket.service.impl;

import com.sipl.ticket.core.dao.entity.ProductCategories;
import com.sipl.ticket.core.dao.entity.ProductSubCategories;
import com.sipl.ticket.core.dao.repository.ProductCategoryRepository;
import com.sipl.ticket.core.dao.repository.ProductSubCategoryRepository;
import com.sipl.ticket.core.dto.request.ProductSubCategoryRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.ProductSubCategoryDto;
import com.sipl.ticket.core.helper.ProductSubCategoryExcelGenerator;
import com.sipl.ticket.core.mapper.ProductSubCategoryMapper;
import com.sipl.ticket.service.ProductSubCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
public class ProductSubCategoryServiceImpl
        implements ProductSubCategoryService {

    private final ProductSubCategoryRepository repository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductSubCategoryMapper mapper;

    @Override
    @CacheEvict(value = "productSubCategories", allEntries = true)
    public ApiResponseDTO<ProductSubCategoryDto> saveProductSubCategory(
            ProductSubCategoryRequestDto dto) {

        log.info("Saving product sub category with name: {}", dto.getProductSubCategoryName());

        try {
            String name = dto.getProductSubCategoryName().trim();

            if (repository.existsByProductSubCategoryNameIgnoreCase(name)) {
                return new ApiResponseDTO<>(
                        null,
                        "Product sub category '" + name + "' already exists.",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            ProductCategories category =
                    productCategoryRepository.findById(dto.getProductCategoryId())
                            .orElse(null);

            if (category == null || Boolean.FALSE.equals(category.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Invalid or inactive product category",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            ProductSubCategories subCategory = new ProductSubCategories();
            subCategory.setProductCategories(category);
            subCategory.setProductSubCategoryName(name);
            subCategory.setIsActive(true);

            ProductSubCategories saved = repository.save(subCategory);

            return new ApiResponseDTO<>(
                    mapper.toDto(saved),
                    "Product sub category created successfully",
                    HttpStatus.CREATED,
                    false
            );

        } catch (Exception e) {
            log.error("Error occurred while saving product sub category", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @CacheEvict(value = "productSubCategories", allEntries = true)
    public ApiResponseDTO<ProductSubCategoryDto> updateProductSubCategory(
            ProductSubCategoryRequestDto dto) {

        log.info(
                "Updating product sub category, id={}, name={}",
                dto.getProductSubCategoryId(),
                dto.getProductSubCategoryName()
        );

        try {
            if (dto == null || dto.getProductSubCategoryId() == null ||
                    dto.getProductSubCategoryName() == null ||
                    dto.getProductSubCategoryName().trim().isEmpty()) {

                return new ApiResponseDTO<>(
                        null,
                        "Product sub category ID and name are required",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            ProductSubCategories subCategory =
                    repository.findById(dto.getProductSubCategoryId()).orElse(null);

            if (subCategory == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Product sub category not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.FALSE.equals(subCategory.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Inactive product sub category cannot be updated",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            String name = dto.getProductSubCategoryName().trim();

            if (repository.existsByProductSubCategoryNameIgnoreCaseAndProductSubCategoryIdNot(
                    name, dto.getProductSubCategoryId())) {

                return new ApiResponseDTO<>(
                        null,
                        "Product sub category with the name '" + name + "' already exists.",
                        HttpStatus.CONFLICT,
                        true
                );
            }

            subCategory.setProductSubCategoryName(name);
            ProductSubCategories updated = repository.save(subCategory);

            return new ApiResponseDTO<>(
                    mapper.toDto(updated),
                    "Product sub category updated successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("updateProductSubCategory unexpected error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<ProductSubCategoryDto> getById(Long id) {

        log.info("Fetching product sub category by id={}", id);

        try {
            return repository.findById(id)
                    .filter(sc -> Boolean.TRUE.equals(sc.getIsActive()))
                    .map(sc -> new ApiResponseDTO<>(
                            mapper.toDto(sc),
                            "Product sub category found",
                            HttpStatus.OK,
                            false
                    ))
                    .orElseGet(() -> new ApiResponseDTO<>(
                            null,
                            "Product sub category not found",
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
    @CacheEvict(value = "productSubCategories", allEntries = true)
    public ApiResponseDTO<String> deleteById(Long id) {

        log.info("Deactivating product sub category, id={}", id);

        try {
            ProductSubCategories subCategory =
                    repository.findById(id).orElse(null);

            if (subCategory == null) {
                return new ApiResponseDTO<>(
                        null,
                        "Product sub category not found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            if (Boolean.FALSE.equals(subCategory.getIsActive())) {
                return new ApiResponseDTO<>(
                        null,
                        "Product sub category is already inactive",
                        HttpStatus.BAD_REQUEST,
                        true
                );
            }

            subCategory.setIsActive(false);
            repository.save(subCategory);

            return new ApiResponseDTO<>(
                    null,
                    "Product sub category deleted successfully",
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
    @Cacheable("productSubCategories")
    public ApiResponseDTO<PagedResponse<ProductSubCategoryDto>>
    getAllProductSubCategories() {

        log.info("Fetching all active product sub categories");

        try {
            List<ProductSubCategoryDto> response = repository.findAll()
                    .stream()
                    .filter(sc -> Boolean.TRUE.equals(sc.getIsActive()))
                    .map(mapper::toDto)
                    .collect(Collectors.toList());

            if (response.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No product sub categories found",
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
                    "Product sub categories fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("getAllProductSubCategories unexpected error", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }
    @Override
    public void exportProductSubCategoriesExcel(
            HttpServletResponse response) {

        log.info("Exporting active product sub categories to Excel");

        try {
            List<ProductSubCategoryDto> subCategories =
                    repository.findAll()
                            .stream()
                            .filter(s -> Boolean.TRUE.equals(s.getIsActive()))
                            .map(mapper::toDto)
                            .collect(Collectors.toList());

            ProductSubCategoryExcelGenerator
                    .generateExcel(subCategories, response);

            log.info(
                    "Product Sub Category Excel export completed successfully, totalRecords={}",
                    subCategories.size()
            );

        } catch (Exception e) {
            log.error(
                    "exportProductSubCategoriesExcel unexpected error", e);
            throw new RuntimeException(
                    "Failed to export product sub categories Excel", e
            );
        }
    }
}
