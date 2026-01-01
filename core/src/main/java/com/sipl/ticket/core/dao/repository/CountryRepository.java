package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
