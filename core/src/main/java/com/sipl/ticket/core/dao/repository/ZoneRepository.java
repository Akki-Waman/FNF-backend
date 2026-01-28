package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {

    @Query(
            "from Zone z " +
                    "where z.isActive = true " +
                    "order by z.zoneName asc"
    )
    List<Zone> findByIsActiveTrue();

    @Query("SELECT z FROM Zone z WHERE z.region.regionId = :regionId AND z.isActive = true")
    List<Zone> findByRegionId(@Param("regionId") Long regionId);

    @Query(
            "SELECT z FROM Zone z " +
                    "WHERE (:companyId IS NULL OR z.company.companyId = :companyId) " +
                    "ORDER BY z.zoneName ASC"
    )
    List<Zone> findZones(
            @Param("companyId") Long companyId
    );

}
