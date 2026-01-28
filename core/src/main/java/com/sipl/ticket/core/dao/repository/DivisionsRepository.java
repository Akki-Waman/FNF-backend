package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Divisions;
import net.bytebuddy.implementation.bytecode.Division;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DivisionsRepository extends JpaRepository<Divisions, Long> {
    @Query(
            "from Divisions d " +
                    "where d.isActive = true " +
                    "order by d.divisionName asc"
    )
    List<Divisions> findByIsActiveTrue();

    @Query("SELECT d FROM Divisions d WHERE d.zone.zoneId = :zoneId AND d.isActive = true")
    List<Divisions> findByZoneId(@Param("zoneId") Long zoneId);

    @Query(
            "SELECT d FROM Divisions d " +
                    "WHERE (:companyId IS NULL OR d.company.companyId = :companyId) " +
                    "ORDER BY d.divisionName ASC"
    )
    List<Divisions> findDivisions(
            @Param("companyId") Long companyId
    );

}
