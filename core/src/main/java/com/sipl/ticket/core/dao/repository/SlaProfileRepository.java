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


    @Query(
            "SELECT CASE WHEN COUNT(sp) > 0 THEN TRUE ELSE FALSE END " +
                    "FROM SlaProfile sp " +
                    "WHERE LOWER(sp.profileName) = LOWER(:profileName) " +
                    "AND sp.branch.branchId = :branchId " +
                    "AND sp.slaProfileId <> :slaProfileId " +
                    "AND sp.isDeleted = false"
    )
    boolean existsActiveProfileForBranchAndNotId(
            @Param("profileName") String profileName,
            @Param("branchId") Integer branchId,
            @Param("slaProfileId") Integer slaProfileId
    );


    @Query(
            "SELECT CASE WHEN COUNT(sp) > 0 THEN TRUE ELSE FALSE END " +
                    "FROM SlaProfile sp " +
                    "WHERE LOWER(sp.profileName) = LOWER(:profileName) " +
                    "AND sp.branch.branchId = :branchId " +
                    "AND sp.isDeleted = false"
    )
    boolean existsActiveProfileForBranch(
            @Param("profileName") String profileName,
            @Param("branchId") Integer branchId
    );


    @Query(
            " SELECT sp " +
                    " FROM SlaProfile sp " +
                    " WHERE (:slaProfileId IS NULL OR sp.slaProfileId = :slaProfileId) " +
                    " AND (:branchId IS NULL OR sp.branch.branchId = :branchId) " +
                    " AND (:isActive IS NULL OR sp.isActive = :isActive) " +
                    " AND (:profileName IS NULL OR LOWER(sp.profileName) LIKE LOWER(CONCAT('%', :profileName, '%'))) "
    )
    Page<SlaProfile> searchSlaProfiles(
            @Param("slaProfileId") Integer slaProfileId,
            @Param("branchId") Integer branchId,
            @Param("isActive") Boolean isActive,
            @Param("profileName") String profileName,
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
