package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Locations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Locations, Long> {

    boolean existsByLocationNameIgnoreCase(String locationName);

    boolean existsByLocationNameIgnoreCaseAndLocationIdNot(
            String locationName, Long locationId
    );

    List<Locations> findByIsActiveTrue();

    Optional<Locations> findByLocationId(Long locationId);
}
