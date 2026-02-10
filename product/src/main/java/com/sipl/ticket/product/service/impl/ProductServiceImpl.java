package com.sipl.ticket.product.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sipl.client.dms.dto.response.DmsResponseDTO;
import com.sipl.client.dms.dto.response.DocumentDTO;
import com.sipl.client.dms.impl.DocumentClientService;
import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.core.dao.entity.*;
import com.sipl.ticket.core.dao.repository.*;
import com.sipl.ticket.core.dto.request.NewProductRequestDto;
import com.sipl.ticket.core.dto.request.ProductSearchRequestDto;
import com.sipl.ticket.core.dto.response.*;
import com.sipl.ticket.core.exception.custom.CustomException;
import com.sipl.ticket.core.exception.custom.ProductNotFoundException;
import com.sipl.ticket.core.exception.custom.ResourceNotFoundException;
import com.sipl.ticket.core.helper.ProductExcelGenerator;
import com.sipl.ticket.core.mapper.ProductMapper;
import com.sipl.ticket.core.mapper.ProductUnitMapper;
import com.sipl.ticket.core.util.ApkUtil;
import com.sipl.ticket.core.util.FileUploadUtil;
import com.sipl.ticket.core.util.PaginationUtil;
import com.sipl.ticket.product.service.ProductService;
import com.sipl.ticket.product.service.ProductUnitService;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Cacheable;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ObjectMapper objectMapper;
    private final FileUploadUtil fileUploadUtil;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductUnitService productUnitService;
    private final SettingRepository settingRepository;
    private final DocumentClientService documentClientService;
    private final ProductCategoryRepository productCategoriesRepository;
    private final ProductSubCategoryRepository productSubCategoryRepository;
    private final BrandRepository brandsRepository;
    private final GstSlabRepository gstSlabRepository;
    private final UnitRepository unitRepository;
    private final OriginsRepository originsRepository;
    private final AccountRepository accountRepository;
    private final ProductUnitRepository productUnitRepository;
    private final BranchRepository branchesRepository;
    private final ProductUnitMapper productUnitMapper;
    private final ApkUtil apkUtil;
    private final ExcelReaderService excelReaderService;


    @Override
    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    @ActivityLoggable(
            action = "CREATE",
            module = "PRODUCT",
            description = "Product {0} created successfully"
    )
    public ApiResponseDTO<CombinedProductResponseDto> saveOrUpdateProduct(
            Long productId, String productRequestDtoString, MultipartFile multipartFile) {
        log.info("Incoming productRequestDtoString = {}", productRequestDtoString);

        try {
            NewProductRequestDto productRequestDto =
                    objectMapper.readValue(productRequestDtoString, NewProductRequestDto.class);
            if (productRequestDto.getProductDto() == null) {
                return new ApiResponseDTO<>(
                        null, null, null, "Product DTO cannot be null.", HttpStatus.BAD_REQUEST, true, null, null);
            }
            if (productRequestDto.getProductDto().getProductCategory() == null) {
                return new ApiResponseDTO<>(
                        null,
                        null,
                        null,
                        "Product category cannot be null.",
                        HttpStatus.BAD_REQUEST,
                        true,
                        null,
                        null
                );

            }
            validateProductUnits(
                    productRequestDto.getProductUnitDtoList(), productRequestDto.getProductDto().getUnit());
            normalizeBooleanFields(productRequestDto);
            log.info("validateProductUnits");
            checkIfProductCodeExists(productRequestDto, productId);
            log.info("checkIfProductCodeExists");
            Products productToBeSaved;
            if (productId == null) {
                Optional<Products> existingProduct =
                        productRepository.findByProductName(productRequestDto.getProductDto().getProductName());
                if (existingProduct.isPresent()) {
                    return new ApiResponseDTO<>(
                            null,
                            null,
                            null,
                            "Product with the name: "
                                    + existingProduct.get().getProductName()
                                    + " already exists.",
                            HttpStatus.CONFLICT,
                            true,
                            null,
                            null
                    );

                }
                log.info("productToBeSaved");
                productToBeSaved = productMapper.toEntity(productRequestDto.getProductDto());
                productToBeSaved.setIsDelete(false);
                productToBeSaved.setIsActive(true);
                String generatedCode = generateProductCode(productRequestDto, productToBeSaved);
                log.info("generatedCode");
                productToBeSaved.setProductCode(generatedCode);
                log.info("setProductCode");

            } else {
                Products existingProductFromDb =
                        productRepository
                                .findByProductId(productId)
                                .orElseThrow(
                                        () ->
                                                new ProductNotFoundException(
                                                        "No Product found for the specified productId"));
                if (Boolean.TRUE.equals(existingProductFromDb.getIsDelete())) {
                    throw new IllegalArgumentException("Cannot update deleted product");
                }

                log.info("existingProductFromDb");

                productToBeSaved =
                        productMapper.updateEntity(existingProductFromDb, productRequestDto.getProductDto());
            }
            setNestedEntities(productToBeSaved, productRequestDto.getProductDto());
            if (productRequestDto.getProductDto().getIsActive() == null) {
                productToBeSaved.setIsActive(true);
            } else {
                productToBeSaved.setIsActive(productRequestDto.getProductDto().getIsActive());
            }
            log.info("setNestedEntities");

            if (productRequestDto.getProductDto().getDefaultTaxHead() != null) {
                Long gstSlabId = productRequestDto.getProductDto().getDefaultTaxHead().getSlabId();
                GstSlabMaster gstSlabMaster =
                        gstSlabRepository
                                .findById(gstSlabId)
                                .orElseThrow(() -> new CustomException("GST slab is required"));

                log.info("gstSlabMaster");
                productToBeSaved.setDefaultTaxHead(gstSlabMaster);
            }
            productToBeSaved.setIsSync(false);
            log.info("saved");
            Products productSavedToDb = productRepository.save(productToBeSaved);
            List<ProductUnitDto> savedProductUnitDtoList =
                    productUnitService.saveProductUnits(
                            productRequestDto.getProductUnitDtoList(), productSavedToDb);
            log.info("savedProductUnitDtoList");

            if (multipartFile != null && !multipartFile.isEmpty()) {
//                try {
//                    log.info(
//                            "Uploading document [{}] to DMS for product ID: {}",
//                            multipartFile.getOriginalFilename(),
//                            productSavedToDb.getProductId());
//                    Long dmsDocumentId = uploadDocToDms(multipartFile);
//                    productSavedToDb.setDmsDocId(dmsDocumentId);
//                    log.info("DMS upload successful. Document ID: {}", dmsDocumentId);
//                } catch (Exception e) {
//                    log.error(
//                            "Failed to upload product document [{}] to DMS: {}",
//                            multipartFile.getOriginalFilename(),
//                            e.getMessage());
//                    throw new CustomException(
//                            "Failed to upload product document to DMS: " + multipartFile.getOriginalFilename());
//                }
            }
            if (multipartFile != null) {
                String moduleName = "products";
                String savedFileName =
                        fileUploadUtil.saveFile(
                                multipartFile,
                                productSavedToDb.getProductId(),
                                productSavedToDb.getProductName(),
                                moduleName);
                productSavedToDb.setFileName(savedFileName);
                log.info("productSavedToDb");
                productRepository.save(productSavedToDb);
            }
            ProductDto savedProductDto = productMapper.toDto(productSavedToDb);
            log.info("savedProductDto");
            if (savedProductDto.getProductCategory() != null
                    && productSavedToDb.getProductCategory() != null) {
                savedProductDto
                        .getProductCategory()
                        .setProductCategoryName(productSavedToDb.getProductCategory().getProductCategoryName());
                savedProductDto
                        .getProductCategory()
                        .setIsActive(productSavedToDb.getProductCategory().getIsActive());
            }
            CombinedProductResponseDto combinedProductResponseDto =
                    new CombinedProductResponseDto(savedProductDto, savedProductUnitDtoList);
            log.info("combinedProductResponseDto");

            String successMessage =
                    (productId != null)
                            ? "Product " + savedProductDto.getProductName() + " updated successfully."
                            : "New Product created successfully.";

            HttpStatus successStatus = (productId != null) ? HttpStatus.OK : HttpStatus.CREATED;

            return new ApiResponseDTO<>(
                    combinedProductResponseDto,
                    null,
                    null,
                    successMessage,
                    successStatus,
                    false,
                    null,
                    null
            );


        } catch (IllegalArgumentException e) {
            return new ApiResponseDTO<>(
                    null,
                    null,
                    null,
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    true,
                    null,
                    null
            );
        } catch (Exception e) {
            log.error("Exception occurred at saveOrUpdateProduct: ", e);
            return new ApiResponseDTO<>(
                    null,
                    null,
                    null,
                    "Internal Server Error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true,
                    null,
                    null
            );
        }
    }

    private void validateProductUnits(List<ProductUnitDto> productUnitDtoList, UnitDto defaultUnit) {
        if (productUnitDtoList == null || productUnitDtoList.isEmpty()) {
            throw new IllegalArgumentException("At least one unit is mandatory.");
        }
        // Check if the default unit exists in the list
        boolean defaultUnitExists =
                productUnitDtoList.stream()
                        .anyMatch(unitDto -> unitDto.getUnit().getUnitId().equals(defaultUnit.getUnitId()));

        if (!defaultUnitExists) {
            throw new IllegalArgumentException("Default unit must be included in the unit list.");
        }

        Set<Long> uniqueUnitIds = new HashSet<>();
        for (ProductUnitDto unitDto : productUnitDtoList) {
            Long unitId = unitDto.getUnit().getUnitId();
            String unitName = unitDto.getUnit().getUnitName();

            // Check for duplicate unit
            if (!uniqueUnitIds.add(unitId)) {
                throw new IllegalArgumentException(
                        "Duplicate unit can not be added with unit name: " + unitName + ".");
            }
        }
    }

    private void normalizeBooleanFields(NewProductRequestDto productRequestDto) {
        if (productRequestDto.getProductDto().getIsService() == null) {
            productRequestDto.getProductDto().setIsService(false);
        }
        if (productRequestDto.getProductUnitDtoList() != null) {
            productRequestDto
                    .getProductUnitDtoList()
                    .forEach(
                            unit -> {
                                if (unit.getIsPurchaseUnit() == null) {
                                    unit.setIsPurchaseUnit(false);
                                }
                                if (unit.getIsSellingUnit() == null) {
                                    unit.setIsSellingUnit(false);
                                }
                            });
        }
    }

    private void checkIfProductCodeExists(NewProductRequestDto productRequestDto, Long productId) {
        if (productId == null) {
            Optional<Products> existingProductByCode =
                    productRepository.findByProductCode(productRequestDto.getProductDto().getProductCode());

            if (existingProductByCode.isPresent()) {
                throw new IllegalArgumentException(
                        "Product with product code: "
                                + productRequestDto.getProductDto().getProductCode()
                                + " already exists.");
            }
        }
    }

    private String generateProductCode(
            NewProductRequestDto productRequestDto, Products productToBeSaved) {
        Setting setting =
                settingRepository
                        .findByScreen("PRODUCT")
                        .orElseThrow(() -> new RuntimeException("Setting not configured for PRODUCT screen"));

        if (Boolean.TRUE.equals(setting.getIsManual())) {
            String dtoCode = productRequestDto.getProductDto().getProductCode();

            if (dtoCode == null || dtoCode.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Product code is required when manual setting is enabled.");
            }

            if (productRepository.existsByProductCode(dtoCode)) {
                throw new IllegalArgumentException("Product code already exists.");
            }

            return dtoCode;
        } else {
            String prefix = setting.getPrefix();
            if (prefix == null || prefix.trim().isEmpty()) {
                throw new IllegalArgumentException("Prefix not configured in Setting for PRODUCT screen.");
            }

            Long nextNumber = setting.getLastNumber() + 1;
            String generatedCode = prefix + String.format("%05d", nextNumber); // e.g., "PRD00001"

            setting.setLastNumber(nextNumber);
            settingRepository.save(setting);

            return generatedCode;
        }
    }

    private void setNestedEntities(Products productEntity, ProductDto productDto) {
        if (productDto.getBranchId() == null) {
            throw new IllegalArgumentException("Branch is required.");
        }

        Branches branch =
                branchesRepository
                        .findByBranchId(productDto.getBranchId())
                        .orElseThrow(() ->
                                new IllegalArgumentException("Invalid branch id"));

        productEntity.setBranch(branch);
        if (productDto.getProductSubCategory() != null) {

            Optional<ProductSubCategories> productSubCategoryFromDb =
                    productSubCategoryRepository.findById(
                            productDto.getProductSubCategory().getProductSubCategoryId());
            productSubCategoryFromDb.ifPresent(productEntity::setProductSubCategory);
        }
        if (productDto.getBrands() != null) {
            Optional<Brands> brandFromDb =
                    brandsRepository.findById(productDto.getBrands().getBrandId());
            brandFromDb.ifPresent(productEntity::setBrands);
        }
        if (productDto.getOrigins() != null) {
            Optional<Origins> originFromDb =
                    originsRepository.findById(productDto.getOrigins().getOriginId());
            originFromDb.ifPresent(productEntity::setOrigins);
        }
        if (productDto.getUnit() != null) {
            Optional<Unit> unitFromDB = unitRepository.findActiveById(productDto.getUnit().getUnitId());
            unitFromDB.ifPresent(productEntity::setUnit);
        }
        if (productDto.getAccount() != null && productDto.getAccount().getAccountId() != null) {
            accountRepository
                    .findById(productDto.getAccount().getAccountId())
                    .ifPresent(productEntity::setAccount);
        } else {
            productEntity.setAccount(null);
        }
        if (productDto.getDefaultTaxHead() != null) {
            Optional<GstSlabMaster> gstFromDB =
                    gstSlabRepository.findActiveGstById(productDto.getDefaultTaxHead().getSlabId());
            gstFromDB.ifPresent(productEntity::setDefaultTaxHead);
        }
        if (productDto.getProductCategory() != null) {
            if (productDto.getProductCategory().getProductCategoryId() == null) {
                throw new IllegalArgumentException("Product category is missing.");
            }
            Optional<ProductCategories> productCategoryFromDb =
                    productCategoriesRepository.findById(
                            productDto.getProductCategory().getProductCategoryId());
            productCategoryFromDb.ifPresent(productEntity::setProductCategory);
        }
    }

//    private Long uploadDocToDms(MultipartFile multipartFile) {
//        log.info("Uploading document to DMS: {}", multipartFile.getOriginalFilename());
//        DmsResponseDTO<?> response = documentClientService.upload(multipartFile);
//
//        if (response == null || response.getData() == null) {
//            log.error("DMS upload failed: null response or missing data");
//            throw new RuntimeException("Document upload failed. No valid response from DMS service.");
//        }
//
//        DocumentDTO documentDTO = objectMapper.convertValue(response.getData(), DocumentDTO.class);
//        log.info("Uploaded Document ID from DMS: {}", documentDTO.getDocumentId());
//        return documentDTO.getDocumentId();
//    }


    @Override
    @ActivityLoggable(
            action = "UPDATE",
            module = "PRODUCT",
            description = "Product id {0} updated successfully"
    )
    public ApiResponseDTO<ProductDto> deleteProduct(Long productId) {
        try {

            List<ProductUnit> productUnit = productUnitRepository.findAllByProductId(productId);

            if (!productUnit.isEmpty()) {
                productUnitRepository.deleteByProductId(productId);
            }

            Optional<Products> products = productRepository.findByProductId(productId);
            if (!products.isPresent()) {
                return new ApiResponseDTO<>(
                        null,
                        null,
                        null,
                        "Product not found",
                        HttpStatus.NOT_FOUND,
                        true,
                        null,
                        null
                );
            }
            Products productsToSave = products.get();
            productsToSave.setIsActive(false);
            productsToSave.setIsDelete(true);
            Products product = productRepository.save(productsToSave);
            ProductDto productToBeSent = productMapper.toDto(product);
            return new ApiResponseDTO<>(
                    productToBeSent,
                    null,
                    null,
                    "Product deleted successfully.",
                    HttpStatus.OK,
                    true,
                    null,
                    null
            );
        } catch (Exception e) {
            log.error("Exception occured at deleteProduct. ", e);
        }
        return new ApiResponseDTO<>(
                null,
                null,
                null,
                "INTERNAL_SERVER_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR,
                true,
                null,
                null
        );
    }

    @Override
    public ApiResponseDTO<CombinedProductResponseDto> getByProduct(Long productId) {
        log.info("Get product request received. productId={}", productId);
        try {
            Products product = productRepository.findByProductId(productId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Product not found")
                    );
            ProductDto productDto = productMapper.toDto(product);
            CombinedProductResponseDto combinedDto =
                    new CombinedProductResponseDto();
            combinedDto.setProductDto(productDto);
            log.debug("Fetching product units for productId={}", productId);
            List<ProductUnit> productUnits =
                    productUnitRepository.findAllByProductId(productId);
            if (productUnits != null && !productUnits.isEmpty()) {
                List<ProductUnitDto> productUnitDtos =
                        productUnitMapper.toProductUnitDtoList(productUnits);
                combinedDto.setProductUnitDtoList(productUnitDtos);
            }
            log.info("Product fetched successfully with units. productId={}", productId);
            return new ApiResponseDTO<>(
                    combinedDto,
                    "Product found successfully",
                    HttpStatus.OK,
                    false
            );
        } catch (Exception e) {
            log.error("Error occurred while fetching product. productId={}", productId, e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<ProductDto> getAllProduct(Integer branchId) {
        try {
            List<Products> productList = productRepository.findByIsActiveTrue(branchId);

            if (productList.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No products found",
                        HttpStatus.NO_CONTENT,
                        false
                );
            }

            List<ProductDto> productDtoList = productList.stream()
                    .map(productMapper::toDto)
                    .collect(Collectors.toList());

            return new ApiResponseDTO<>(
                    null,
                    productDtoList,
                    null,
                    "Products fetched successfully",
                    HttpStatus.OK,
                    false,
                    null,
                    null
            );

        } catch (Exception e) {
            log.error("Exception occurred at getAllProduct", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<PagedResponse<CombinedProductResponseDto>> searchProducts(
            ProductSearchRequestDto dto) {

        try {
            log.info("Searching products with request: {}", dto);

            String sortBy = dto.getSortBy();
            if ("productSubCategoryId".equalsIgnoreCase(sortBy)) {
                sortBy = "productSubCategory.productSubCategoryId";
            } else if ("productCategoryId".equalsIgnoreCase(sortBy)) {
                sortBy = "productCategory.productCategoryId";
            } else if ("brandId".equalsIgnoreCase(sortBy)) {
                sortBy = "brands.brandId";
            } else if ("originId".equalsIgnoreCase(sortBy)) {
                sortBy = "origins.originId";
            } else if ("branchId".equalsIgnoreCase(sortBy)) {
                sortBy = "branch.branchId";
            } else {
                sortBy = "productName";
            }

            Pageable pageable = PageRequest.of(
                    dto.getPage(),
                    dto.getSize(),
                    Sort.by(
                            Sort.Direction.fromString(dto.getSortDir()),
                            sortBy
                    )
            );

            Page<Products> pageResult = productRepository.searchProducts(
                    dto.getProductName(),
                    dto.getBrandId(),
                    dto.getOriginId(),
                    dto.getProductCategoryId(),
                    dto.getProductSubCategoryId(),
                    dto.getBranchId(),
                    pageable
            );

            if (pageResult.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No products found",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }

            List<CombinedProductResponseDto> responseList =
                    pageResult.getContent().stream()
                            .map(product -> {
                                CombinedProductResponseDto combinedDto =
                                        new CombinedProductResponseDto();
                                ProductDto productDto = productMapper.toDto(product);
                                combinedDto.setProductDto(productDto);
                                List<ProductUnit> productUnits =
                                        productUnitRepository.findAllByProductId(productDto.getProductId());
                                if (!productUnits.isEmpty()) {
                                    List<ProductUnitDto> productUnitDtos =
                                            productUnitMapper.toProductUnitDtoList(productUnits);
                                    combinedDto.setProductUnitDtoList(productUnitDtos);
                                }
                                return combinedDto;
                            })
                            .collect(Collectors.toList());

            return new ApiResponseDTO<>(
                    new PagedResponse<>(
                            responseList,
                            pageResult.getNumber(),
                            pageResult.getTotalElements(),
                            pageResult.getTotalPages(),
                            pageResult.getSize(),
                            pageResult.isLast()
                    ),
                    "Products fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("searchProducts error", e);
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
    public void exportProductsExcel(HttpServletResponse response, Integer branchId) {

        log.info("Exporting active products to Excel");

        try {
            List<ProductDto> products = productRepository.findByIsActiveTrue(branchId)
                    .stream()
                    .map(productMapper::toDto)
                    .collect(Collectors.toList());

            ProductExcelGenerator.generateExcel(products, response);

            log.info("Products Excel export completed successfully, totalRecords={}",
                    products.size());

        } catch (Exception e) {
            log.error("exportProductsExcel unexpected error", e);
            throw new RuntimeException("Failed to export products Excel", e);
        }
    }

    @Override
    public ResponseEntity<Resource> downloadProductFile(String fileName) {
        log.info("<<START>> downloadProductFile <<START>>");

        Resource file = apkUtil.downloadFile(fileName);

        if (file == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .body(file);
    }


    @Override
    public ApiResponseDTO<Void> processExcelFile(MultipartFile file) {
        File tempFile = null;

        try {
            if (file == null || file.isEmpty()) {
                return new ApiResponseDTO<>(
                        "Uploaded file is empty",
                        HttpStatus.BAD_REQUEST,
                        true);
            }

            String tempDir = System.getProperty("java.io.tmpdir");
            String filePath =
                    tempDir + File.separator + file.getOriginalFilename();

            tempFile = new File(filePath);
            file.transferTo(tempFile);

            excelReaderService.readAndSaveExcelData(filePath);

            return new ApiResponseDTO<>(
                    "Excel file processed successfully",
                    HttpStatus.OK,
                    false);

        } catch (RuntimeException e) {
            log.error("Runtime exception in processExcelFile", e);
            return new ApiResponseDTO<>(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true);

        } catch (Exception e) {
            log.error("Exception in processExcelFile", e);
            return new ApiResponseDTO<>(
                    "An error occurred while processing the Excel file",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true);

        } finally {
            if (tempFile != null && tempFile.exists()) {
                boolean deleted = tempFile.delete();
                if (!deleted) {
                    log.warn(
                            "Failed to delete temporary file: {}",
                            tempFile.getAbsolutePath());
                }
            }
        }
    }

}

