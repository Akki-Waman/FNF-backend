package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.SlaProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SlaProfileRepository extends JpaRepository<SlaProfile, Integer> {


    boolean existsByProfileNameIgnoreCaseAndBranch_BranchId(
            String profileName,
            Integer branchId
    );

    boolean existsByProfileNameIgnoreCaseAndBranch_BranchIdAndSlaProfileIdNot(
            String profileName,
            Integer branchId,
            Integer slaProfileId
    );


    @Query(
            " SELECT sp " +
                    " FROM SlaProfile sp " +
                    " WHERE (:slaProfileId IS NULL OR sp.slaProfileId = :slaProfileId) " +
                    " AND (:branchId IS NULL OR sp.branch.branchId = :branchId) " +
                    " AND (:isActive IS NULL OR sp.isActive = :isActive) "
    )
    Page<SlaProfile> searchSlaProfiles(
            @Param("slaProfileId") Integer slaProfileId,
            @Param("branchId") Integer branchId,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );

    @Query("SELECT sp FROM SlaProfile sp " +
            "WHERE sp.branch.branchId = :branchId " +
            "AND sp.isActive = true " +
            "AND :today BETWEEN sp.effectiveFrom AND sp.effectiveTo ")
    Optional<SlaProfile> findActiveProfileByBranch(
            @Param("branchId") Integer branchId,
            @Param("today") LocalDate today
    );
}
