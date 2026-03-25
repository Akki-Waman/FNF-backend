package com.sipl.ticket.service.impl;


import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.core.dao.entity.*;
import com.sipl.ticket.core.dao.repository.*;
import com.sipl.ticket.core.dto.request.ClientProductSearchRequestDto;
import com.sipl.ticket.core.dto.request.ClientProductsRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.ClientProductsResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.helper.ClientProductExcelGenerator;
import com.sipl.ticket.core.helper.ClientProductsExportHelper;
import com.sipl.ticket.core.mapper.ClientProductMapper;
import com.sipl.ticket.core.util.PaginationUtil;
import com.sipl.ticket.service.ClientProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
public class ClientProductServiceImpl implements ClientProductService {

    private final ClientProductsRepository clientProductsRepository;
    private final ClientProductMapper clientProductMapper;
    private final OperationalUnitRepository operationalUnitRepository;
    private final ProductRepository productRepository;
    private final RegionRepository regionRepository;
    private final ZoneRepository zoneRepository;
    private final DivisionsRepository divisionsRepository;
    private final BranchRepository branchRepository;

    @ActivityLoggable(
            action = "CREATE",
            module = "CLIENT_PRODUCTS",
            description = "Client product {0} created successfully"
    )
    public ApiResponseDTO<ClientProductsResponseDTO> saveClientProducts(ClientProductsRequestDTO dto) {
        log.info("Saving client product with serialNo: {}, imeiNo: {}",
                dto.getSerialNumber(), dto.getImeiNo());
        try {
            if (StringUtils.hasText(dto.getSerialNumber()) &&
                    clientProductsRepository.existsBySerialNumberIgnoreCase(dto.getSerialNumber().trim())) {
                return new ApiResponseDTO<>(
                        null,
                        "Client product with Serial Number '" + dto.getSerialNumber() + "' already exists.",
                        HttpStatus.CONFLICT,
                        true
                );
            }
            if (StringUtils.hasText(dto.getImeiNo()) &&
                    clientProductsRepository.existsByImeiNoIgnoreCase(dto.getImeiNo().trim())) {
                return new ApiResponseDTO<>(
                        null,
                        "Client product with IMEI No '" + dto.getImeiNo() + "' already exists.",
                        HttpStatus.CONFLICT,
                        true
                );
            }
            ClientProducts entity = clientProductMapper.toEntity(dto);
            ApiResponseDTO<Object> relationResponse = setRelations(dto, entity);
            if (relationResponse != null) {
                return new ApiResponseDTO<>(
                        null,
                        relationResponse.getMessage(),
                        relationResponse.getStatus(),
                        true
                );
            }
            ClientProducts savedEntity = clientProductsRepository.save(entity);
            ClientProductsResponseDTO responseDto = clientProductMapper.toDto(savedEntity);
            return new ApiResponseDTO<>(
                    responseDto,
                    "Client product saved successfully",
                    HttpStatus.CREATED,
                    false
            );
        } catch (Exception e) {
            log.error("Error while saving client product", e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    private ApiResponseDTO<Object> setRelations(ClientProductsRequestDTO dto, ClientProducts entity) {
        if (dto.getProduct() != null && dto.getProduct().getProductId() != null) {
            Optional<Products> optionalProduct = productRepository.findById(dto.getProduct().getProductId());
            if (!optionalProduct.isPresent()) {
                return new ApiResponseDTO<>(null,
                        "Product with ID " + dto.getProduct().getProductId() + " does not exist.",
                        HttpStatus.BAD_REQUEST,
                        true);
            }
            entity.setProducts(optionalProduct.get());
        }
        if (dto.getRegion() != null && dto.getRegion().getRegionId() != null) {
            Optional<Region> optionalRegion = regionRepository.findById(dto.getRegion().getRegionId());
            if (!optionalRegion.isPresent()) {
                return new ApiResponseDTO<>(null,
                        "Region with ID " + dto.getRegion().getRegionId() + " does not exist.",
                        HttpStatus.BAD_REQUEST,
                        true);
            }
            entity.setRegion(optionalRegion.get());
        }
        if (dto.getZone() != null && dto.getZone().getZoneId() != null) {
            Optional<Zone> optionalZone = zoneRepository.findById(dto.getZone().getZoneId());
            if (!optionalZone.isPresent()) {
                return new ApiResponseDTO<>(null,
                        "Zone with ID " + dto.getZone().getZoneId() + " does not exist.",
                        HttpStatus.BAD_REQUEST,
                        true);
            }
            entity.setZone(optionalZone.get());
        }
        if (dto.getDivision() != null && dto.getDivision().getDivisionId() != null) {
            Optional<Divisions> optionalDivision = divisionsRepository.findById(dto.getDivision().getDivisionId());
            if (!optionalDivision.isPresent()) {
                return new ApiResponseDTO<>(null,
                        "Division with ID " + dto.getDivision().getDivisionId() + " does not exist.",
                        HttpStatus.BAD_REQUEST,
                        true);
            }
            entity.setDivision(optionalDivision.get());
        }
        if (dto.getUnit() != null && dto.getUnit().getOperationalUnitId() != null) {
            Optional<OperationalUnit> optionalUnit = operationalUnitRepository.findById(dto.getUnit().getOperationalUnitId());
            if (!optionalUnit.isPresent()) {
                return new ApiResponseDTO<>(null,
                        "Operational Unit with ID " + dto.getUnit().getOperationalUnitId() + " does not exist.",
                        HttpStatus.BAD_REQUEST,
                        true);
            }
            entity.setUnit(optionalUnit.get());
        }
        if (dto.getBranchId() != null) {
            Optional<Branches> branchesOptional = branchRepository.findById(dto.getBranchId());
            if (!branchesOptional.isPresent()) {
                return new ApiResponseDTO<>(null,
                        "Branch with ID " + dto.getBranchId() + " does not exist.",
                        HttpStatus.BAD_REQUEST,
                        true);
            }
            entity.setBranch(branchesOptional.get());
        }

        return null;
    }


    @ActivityLoggable(
            action = "UPDATE",
            module = "CLIENT_PRODUCTS",
            description = "Client product {0} updated successfully"
    )
    public ApiResponseDTO<ClientProductsResponseDTO> updateClientProducts(Long clientProductId, ClientProductsRequestDTO dto) {
        log.info("Updating client product with serialNo: {}, imeiNo: {}", dto.getSerialNumber(), dto.getImeiNo());
        try {
            Optional<ClientProducts> optionalEntity = clientProductsRepository.findById(clientProductId);
            if (!optionalEntity.isPresent()) {
                return new ApiResponseDTO<>(
                        null,
                        "Client product with ID " + clientProductId + " does not exist.",
                        HttpStatus.NOT_FOUND,
                        true
                );
            }
            ClientProducts entity = optionalEntity.get();
            if (StringUtils.hasText(dto.getSerialNumber()) &&
                    clientProductsRepository.existsBySerialNumberIgnoreCaseAndClientProductIdNot(
                            dto.getSerialNumber().trim(), clientProductId)) {
                return new ApiResponseDTO<>(
                        null,
                        "Client product with Serial Number '" + dto.getSerialNumber() + "' already exists.",
                        HttpStatus.CONFLICT,
                        true
                );
            }
            if (StringUtils.hasText(dto.getImeiNo()) &&
                    clientProductsRepository.existsByImeiNoIgnoreCaseAndClientProductIdNot(
                            dto.getImeiNo().trim(), clientProductId)) {
                return new ApiResponseDTO<>(
                        null,
                        "Client product with IMEI No '" + dto.getImeiNo() + "' already exists.",
                        HttpStatus.CONFLICT,
                        true
                );
            }
            clientProductMapper.partialUpdate(dto, entity);
            ApiResponseDTO<Object> relationValidation = setRelations(dto, entity);
            if (relationValidation != null) {
                return new ApiResponseDTO<>(
                        null,
                        relationValidation.getMessage(),
                        relationValidation.getStatus(),
                        true
                );
            }
            ClientProducts updatedEntity = clientProductsRepository.save(entity);
            ClientProductsResponseDTO responseDto = clientProductMapper.toDto(updatedEntity);
            return new ApiResponseDTO<>(
                    responseDto,
                    "Client product updated successfully",
                    HttpStatus.OK,
                    false
            );
        } catch (Exception e) {
            log.error("Error while updating client product", e);
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
            module = "CLIENT PRODUCTS",
            description = "Client product id {0} deleted successfully"
    )
    public ApiResponseDTO<ClientProductsResponseDTO> deleteClientProducts(Long clientProductId) {
        try {
            Optional<ClientProducts> optionalClientProduct =
                    clientProductsRepository.findById(clientProductId);
            if (!optionalClientProduct.isPresent()) {
                return new ApiResponseDTO<>(
                        null,
                        "Client Product not found with id : " + clientProductId,
                        HttpStatus.NOT_FOUND,
                        true
                );
            }
            ClientProducts clientProduct = optionalClientProduct.get();
            clientProduct.setIsActive(false);
            clientProductsRepository.save(clientProduct);
            return new ApiResponseDTO<>(
                    null,
                    "Client Product deleted successfully",
                    HttpStatus.OK,
                    false
            );
        } catch (Exception e) {
            log.error("Error while deleting Client Product with id {}", clientProductId, e);
            return new ApiResponseDTO<>(
                    null,
                    "Internal server error while deleting client product",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    public ApiResponseDTO<PagedResponse<ClientProductsResponseDTO>> getAllClientProducts(
            Integer branchId
    ) {

        log.info("Fetching client products, branchId={}", branchId);

        try {
            List<ClientProducts> products =
                    clientProductsRepository.findClientProducts(branchId);

            if (products.isEmpty()) {
                return new ApiResponseDTO<>(
                        null,
                        "No client products found",
                        HttpStatus.OK,
                        false
                );
            }

            List<ClientProductsResponseDTO> response =
                    clientProductMapper.toDtoList(products);

            return new ApiResponseDTO<>(
                    new PagedResponse<>(
                            response,
                            0,
                            response.size(),
                            1,
                            response.size(),
                            true
                    ),
                    "Client products fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("getAllClientProducts unexpected error", e);

            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }


    @Override
    public ApiResponseDTO<PagedResponse<ClientProductsResponseDTO>> searchClientProducts(
            ClientProductSearchRequestDto requestDto) {

        log.info(
                "ClientProduct search started | query='{}', page={}, size={}, sortBy={}, sortDir={}, isActive={}",
                requestDto.getQuery(),
                requestDto.getPage(),
                requestDto.getSize(),
                requestDto.getSortBy(),
                requestDto.getSortDir(),
                requestDto.getIsActive()
        );

        try {
            Pageable pageable = PaginationUtil.pageable(
                    requestDto.getPage(),
                    requestDto.getSize(),
                    requestDto.getSortBy(),
                    requestDto.getSortDir()
            );

            String keyword = StringUtils.hasText(requestDto.getQuery())
                    ? requestDto.getQuery().trim()
                    : "";

            Page<ClientProducts> pageResult =
                    clientProductsRepository.searchClientProducts(
                            keyword,
                            requestDto.getIsActive(),
                            requestDto.getBranchId(),
                            pageable
                    );

            if (pageResult.isEmpty()) {
                log.warn(
                        "No client products found for search | query='{}'",
                        requestDto.getQuery()
                );

                return new ApiResponseDTO<>(
                        null,
                        "No client products matched your search criteria",
                        HttpStatus.NOT_FOUND,
                        false
                );
            }

            List<ClientProductsResponseDTO> content =
                    pageResult.getContent()
                            .stream()
                            .map(clientProductMapper::toDto)
                            .collect(Collectors.toList());

            log.info(
                    "ClientProduct search success | query='{}', results={}, page={}/{}",
                    requestDto.getQuery(),
                    content.size(),
                    pageResult.getNumber() + 1,
                    pageResult.getTotalPages()
            );

            PagedResponse<ClientProductsResponseDTO> pagedResponse =
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
                    "Client products fetched successfully",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {

            log.error(
                    "Error while searching client products | query='{}'",
                    requestDto.getQuery(),
                    e
            );

            return new ApiResponseDTO<>(
                    null,
                    "Something went wrong while searching client products. Please try again later.",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void exportClientProductsExcel(HttpServletResponse response) {

        log.info("Exporting active client products to Excel");

        try {
            List<ClientProducts> entities =
                    clientProductsRepository.findActiveForExport();

            List<ClientProductsResponseDTO> clientProducts =
                    entities.stream()
                            .map(clientProductMapper::toDto)
                            .collect(Collectors.toList());

            ClientProductExcelGenerator.generateExcel(clientProducts, response);

            log.info("Client products Excel export completed successfully, totalRecords={}",
                    clientProducts.size());

        } catch (Exception e) {
            log.error("exportClientProductsExcel unexpected error", e);
            throw new RuntimeException("Failed to export client products Excel", e);
        }
    }

    @Override
    public ApiResponseDTO<ClientProductsResponseDTO> getById(Long id) {

        log.info("Fetching client product by id={}", id);

        try {
            return clientProductsRepository.findActiveById(id)
                    .map(cp -> {

                        ClientProductsResponseDTO dto = clientProductMapper.toDto(cp);
                        String deviceNameWithSerial = cp.getDeviceName() + " - " + cp.getSerialNumber();
                        dto.setDeviceName(deviceNameWithSerial);

                        return new ApiResponseDTO<>(
                                dto,
                                "Client product found",
                                HttpStatus.OK,
                                false
                        );
                    })
                    .orElseGet(() -> new ApiResponseDTO<>(
                            null,
                            "Client product not found",
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
    public void exportClientProducts(
            ClientProductSearchRequestDto requestDto,
            String format,
            HttpServletResponse response
    ) {
        if (format == null ||
                !List.of("excel", "csv", "pdf")
                        .contains(format.toLowerCase())) {
            throw new IllegalArgumentException("Invalid export format");
        }
        try {
            Pageable pageable = Pageable.unpaged();

            String keyword = "";
            Boolean isActive = null;
            Integer branchId = null;
            if (requestDto != null) {
                keyword = StringUtils.hasText(requestDto.getQuery())
                        ? requestDto.getQuery().trim()
                        : "";
                isActive = requestDto.getIsActive();
                branchId = requestDto.getBranchId();
            }
            Page<ClientProducts> pageResult =
                    clientProductsRepository.searchClientProducts(
                            keyword,
                            isActive,
                            branchId,
                            pageable
                    );
            if (pageResult.isEmpty()) {
                throw new RuntimeException("No data found for export");
            }

            List<ClientProductsResponseDTO> data =
                    pageResult.getContent()
                            .stream()
                            .map(clientProductMapper::toDto)
                            .collect(Collectors.toList());

            ClientProductsExportHelper.export(data, format, response);

            log.info(
                    "Client Products export completed | format={}, records={}",
                    format,
                    data.size()
            );

        } catch (Exception e) {
            log.error("exportClientProducts failed", e);
            throw new RuntimeException(
                    "Failed to export client products report", e
            );
        }
    }


}