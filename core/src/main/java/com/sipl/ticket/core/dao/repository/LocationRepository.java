package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Locations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Locations, Long> {

    boolean existsByLocationNameIgnoreCase(String locationName);

    boolean existsByLocationNameIgnoreCaseAndLocationIdNot(
            String locationName, Long locationId
    );

    List<Locations> findByIsActiveTrue();

    Page<Locations> findByLocationId(Long locationId, Pageable pageable);

    @Query(
            "SELECT l FROM Locations l " +
                    "WHERE (:locationId IS NULL OR l.locationId = :locationId)"
    )
    Page<Locations> searchLocations(
            @Param("locationId") Long locationId,
            Pageable pageable
    );

}
