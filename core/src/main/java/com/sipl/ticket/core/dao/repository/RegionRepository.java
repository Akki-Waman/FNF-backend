package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    @Query(
            "from Region r " +
                    "where r.isActive = true " +
                    "order by r.regionName asc"
    )
    List<Region> findByIsActiveTrue();

    @Query(
            "SELECT r FROM Region r " +
                    "WHERE (:companyId IS NULL OR r.company.companyId = :companyId) " +
                    "ORDER BY r.regionName ASC"
    )
    List<Region> findRegions(
            @Param("companyId") Long companyId
    );
}
