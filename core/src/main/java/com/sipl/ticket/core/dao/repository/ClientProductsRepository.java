package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.ClientProducts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientProductsRepository extends JpaRepository<ClientProducts, Long> {

    boolean existsBySerialNumberIgnoreCase(String serialNumber);

    boolean existsByImeiNoIgnoreCase(String imeiNo);

    boolean existsBySerialNumberIgnoreCaseAndClientProductIdNot(
            String serialNumber,
            Long clientProductId
    );

    boolean existsByImeiNoIgnoreCaseAndClientProductIdNot(
            String imeiNo,
            Long clientProductId
    );

    List<ClientProducts> findByIsActiveTrueOrderByDeviceNameAsc();

    @Query(
            "SELECT cp FROM ClientProducts cp " +
                    "LEFT JOIN cp.products p " +
                    "LEFT JOIN cp.region r " +
                    "LEFT JOIN cp.zone z " +
                    "LEFT JOIN cp.division d " +
                    "LEFT JOIN cp.unit u " +
                    "LEFT JOIN cp.branch b " +
                    "WHERE (:isActive IS NULL OR cp.isActive = :isActive) " +
                    "AND (:branchId IS NULL OR b.branchId = :branchId) " +
                    "AND ( " +
                    "LOWER(cp.deviceName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                    "LOWER(cp.groupName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                    "LOWER(cp.serialNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                    "LOWER(cp.imeiNo) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                    "LOWER(cp.platformModel) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                    "LOWER(cp.mdmAssetNo) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                    "LOWER(cp.partNo) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                    "LOWER(cp.workingStatus) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                    "LOWER(cp.deviceStatus) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                    "LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                    "LOWER(r.regionName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                    "LOWER(z.zoneName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                    "LOWER(d.divisionName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                    "LOWER(u.operationalUnitName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                    ")"
    )
    Page<ClientProducts> searchClientProducts(
            @Param("keyword") String keyword,
            @Param("isActive") Boolean isActive,
            @Param("branchId") Integer branchId,
            Pageable pageable
    );

    @Query(
            "SELECT cp FROM ClientProducts cp " +
                    "JOIN FETCH cp.products " +
                    "WHERE cp.isActive = true"
    )
    List<ClientProducts> findActiveForExport();

    @Query(
            "SELECT cp FROM ClientProducts cp " +
                    "WHERE cp.clientProductId = :id " +
                    "AND cp.isActive = true"
    )
    Optional<ClientProducts> findActiveById(@Param("id") Long id);

    @Query(
            "SELECT cp FROM ClientProducts cp " +
                    "WHERE cp.isActive = true " +
                    "AND (:branchId IS NULL OR cp.branch.branchId = :branchId) " +
                    "ORDER BY cp.deviceName ASC"
    )
    List<ClientProducts> findClientProducts(
            @Param("branchId") Integer branchId
    );



}
