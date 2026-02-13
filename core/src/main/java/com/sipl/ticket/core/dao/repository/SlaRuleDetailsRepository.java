package com.sipl.ticket.core.dao.repository;


import com.sipl.ticket.core.dao.entity.SlaRuleDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SlaRuleDetailsRepository extends JpaRepository<SlaRuleDetails, Integer> {

    @Query(
            "SELECT sr FROM SlaRuleDetails sr " +
                    "WHERE sr.slaProfile.slaProfileId = :profileId " +
                    "AND sr.service.serviceId = :serviceId " +
                    "AND sr.severityMasterId = :severityId " +
                    "AND sr.slaTypeMasterId = :slaTypeId " +
                    "AND sr.isActive = true " +
                    "AND sr.isDeleted = false"
    )
    Optional<SlaRuleDetails> findActiveRule(
            @Param("profileId") Integer profileId,
            @Param("serviceId") Long serviceId,
            @Param("severityId") Integer severityId,
            @Param("slaTypeId") Long slaTypeId
    );

    @Query("SELECT sr FROM SlaRuleDetails sr " +
            "WHERE sr.isDeleted = false " +
            "AND (:slaProfileId IS NULL OR sr.slaProfile.slaProfileId = :slaProfileId) " +
            "AND (:isActive IS NULL OR sr.isActive = :isActive)")
    Page<SlaRuleDetails> findAllNotDeleted(
            @Param("slaProfileId") Integer slaProfileId,
            @Param("isActive") Boolean isActive,
            Pageable pageable);


}
