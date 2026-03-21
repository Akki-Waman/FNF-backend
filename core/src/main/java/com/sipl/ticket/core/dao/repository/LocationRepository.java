package com.sipl.ticket.core.dao.repository;

import com.opencsv.bean.CsvToBean;
import com.sipl.ticket.core.dao.entity.Locations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.xml.stream.Location;
import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Locations, Long> {

    @Query(
            "SELECT CASE WHEN COUNT(l) > 0 THEN TRUE ELSE FALSE END " +
                    "FROM Locations l " +
                    "WHERE LOWER(l.locationName) = LOWER(:locationName) " +
                    "AND l.branch.branchId = :branchId " +
                    "AND l.isDeleted = false"
    )
    boolean existsActiveLocationForBranch(
            @Param("locationName") String locationName,
            @Param("branchId") Integer branchId   // Integer because Branch ID is Integer
    );


    boolean existsByLocationNameIgnoreCaseAndLocationIdNot(
            String locationName, Long locationId
    );

    List<Locations> findByIsActiveTrueAndIsDeletedFalse();

    Page<Locations> findByLocationId(Long locationId, Pageable pageable);

    @Query(
            "SELECT l FROM Locations l " +
                    "WHERE l.isDeleted = false " +
                    "AND ( :branchId IS NULL OR l.branch.branchId = :branchId ) " +
                    "AND ( :isActive IS NULL OR l.isActive = :isActive ) " +
                    "AND ( :query IS NULL OR :query = '' " +
                    "   OR LOWER(l.locationName) LIKE CONCAT('%', LOWER(:query), '%') " +
                    "   OR CAST(l.locationId AS string) LIKE CONCAT('%', :query, '%') " +
                    ")"
    )
    Page<Locations> searchLocations(
            @Param("query") String query,
            @Param("isActive") Boolean isActive,
            @Param("branchId") Integer branchId,
            Pageable pageable
    );

    @Query(
                    "select l from Locations l " +
                    "where l.isActive = true " +
                    "and l.isDeleted = false " +
                    "and ( :branchId is null or l.branch.branchId = :branchId ) " +
                    "order by l.locationName asc"
    )
    List<Locations> findActiveLocationsByBranch(
            @Param("branchId") Integer branchId
    );

    boolean existsByLocationNameIgnoreCaseAndBranch_BranchIdAndLocationIdNotAndIsDeletedFalse(String name, Integer branchId, Long locationId);
}
