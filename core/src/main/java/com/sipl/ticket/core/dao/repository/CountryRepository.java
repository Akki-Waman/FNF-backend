package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Country;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    boolean existsByCountryNameIgnoreCase(String name);

    boolean existsByCountryNameIgnoreCaseAndCountryIdNot(String name, Long countryId);

    /* -------- SEARCH -------- */
    List<Country> findByCountryNameContainingIgnoreCaseAndIsActive(
            String countryName,
            Boolean isActive
    );

    List<Country> findByIsForeignAndIsActive(
            Boolean isForeign,
            Boolean isActive
    );

    Page<Country> findByCountryNameContainingIgnoreCaseAndIsActive(
            String countryName,
            Boolean isActive,
            Pageable pageable);

    Page<Country> findByIsForeignAndIsActive(
            Boolean isForeign,
            Boolean isActive,
            Pageable pageable);

    Page<Country> findByIsActive(
            Boolean isActive,
            Pageable pageable);

    @Query(
            "SELECT c FROM Country c " +
                    "WHERE c.isActive = true " +
                    "AND (:name IS NULL OR LOWER(c.countryName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
                    "AND (:isForeign IS NULL OR c.isForeign = :isForeign) " +
                    "ORDER BY c.countryId DESC"
    )
    Page<Country> searchCountries(
            @Param("name") String name,
            @Param("isForeign") Boolean isForeign,
            Pageable pageable
    );


}
